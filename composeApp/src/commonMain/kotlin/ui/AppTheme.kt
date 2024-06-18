package ui

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
    content: @Composable () -> Unit
) {
    AdaptiveTheme(
        target = theme,
        material = MaterialThemeSpec.Default(),
        cupertino = CupertinoThemeSpec.Default(),
        content = content
    )
}
