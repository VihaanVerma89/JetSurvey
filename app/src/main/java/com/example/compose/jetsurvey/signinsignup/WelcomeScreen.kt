package com.example.compose.jetsurvey.signinsignup

import android.media.tv.TvContract
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.jetsurvey.R
import com.example.compose.jetsurvey.ui.theme.JetSurveyTheme


sealed class WelcomeEvent {
    data class SignInSignUp(val email: String) : WelcomeEvent()
    object SignInAsGuest : WelcomeEvent()
}

@Composable
fun WelcomeScreen(
) {
    Column {
        Branding()
        SignInCreateAccount(onEvent = {}, onFocusChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp))
    }
}


@Composable
fun SignInCreateAccount(
    onEvent: (WelcomeEvent) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val emailState = remember { EmailState() }
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        CompositionLocalProvider(LocalContentAlpha provides androidx.compose.material.ContentAlpha.medium) {
            Text(
                text = stringResource(id = R.string.sign_in_create_account),
                style = MaterialTheme.typography.subtitle2,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(
                    top = 64.dp, bottom = 12.dp
                )
            )
        }

        val onSubmit = {
            if (emailState.isValid) {
                onEvent(WelcomeEvent.SignInSignUp(emailState.text))
            } else {
                emailState.enableShowErrors()
            }
        }
        Email(
            emailState = emailState,
            imeAction = ImeAction.Default,
        )

        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp, bottom = 3.dp)
        ) {
            Text(
                text = stringResource(id = R.string.user_continue),
                style = MaterialTheme.typography.subtitle2
            )
        }

        OrSignInAsGuest()
    }

}

@Composable
fun Branding(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.wrapContentHeight(align = Alignment.CenterVertically)) {
        Logo(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
//                .padding(horizontal = 76.dp)
        )

        Text(
            text = stringResource(id = R.string.app_tagline),
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun Logo(
    modifier: Modifier = Modifier,
    lightTheme: Boolean = MaterialTheme.colors.isLight,
) {

    val assetId = if (lightTheme) {
        R.drawable.ic_logo_light
    } else {
        R.drawable.ic_logo_dark
    }

    Image(
        painter = painterResource(id = assetId),
        modifier = modifier,
        contentDescription = null)

}

@Composable
fun Email(
    emailState: TextFieldState = remember { EmailState() },
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {},
) {
    OutlinedTextField(
        value = emailState.text,
        onValueChange = {

        },
        label = {
            CompositionLocalProvider(LocalContentAlpha provides androidx.compose.material.ContentAlpha.medium) {
                Text(
                    text = stringResource(id = R.string.email),
                    style = MaterialTheme.typography.body2
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {

            },
        textStyle = MaterialTheme.typography.body2,
        isError = emailState.showErrors(),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
        keyboardActions = KeyboardActions(onDone = {
            onImeAction()
        })

    )


}

@Preview
@Composable
fun PreviewWelcomeUI(
) {
    JetSurveyTheme {
        androidx.compose.material.Surface(modifier = Modifier.fillMaxWidth()) {
            WelcomeScreen()
        }
    }
}