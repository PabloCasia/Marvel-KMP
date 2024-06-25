package ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import determineTheme
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveTheme
import io.github.alexzhirkevich.cupertino.adaptive.CupertinoThemeSpec
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.alexzhirkevich.cupertino.adaptive.MaterialThemeSpec
import io.github.alexzhirkevich.cupertino.adaptive.Theme

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AppTheme(
    theme: Theme = determineTheme(),
    content: @Composable () -> Unit,
) {
    val isDarkMOde = isSystemInDarkTheme()
    val colorScheme = if (isDarkMOde) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }
    AdaptiveTheme(
        target = theme,
        material = MaterialThemeSpec(colorScheme = colorScheme),
        cupertino = CupertinoThemeSpec.Default(),
        content = content
    )
}
