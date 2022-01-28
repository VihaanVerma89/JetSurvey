package com.example.compose.jetsurvey.signinsignup

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.jetsurvey.R
import com.example.compose.jetsurvey.ui.theme.JetSurveyTheme

@Composable
fun OrSignInAsGuest(

) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        androidx.compose.material.Surface {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = stringResource(id = R.string.or),
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.paddingFromBaseline(top = 25.dp)
                )
            }

        }
        OutlinedButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 20.dp, bottom = 24.dp
                )) {
            Text(text = stringResource(id = R.string.sign_in_guest))
        }
    }
}

@Composable
fun TextFieldError(
    textError: String,
) {
    Row {
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = textError,
            style = LocalTextStyle.current.copy(color = MaterialTheme.colors.error)
        )
    }
}


@Preview
@Composable
fun PreviewTextFieldError(
) {
    JetSurveyTheme {
        TextFieldError(textError = "some error")
    }
}