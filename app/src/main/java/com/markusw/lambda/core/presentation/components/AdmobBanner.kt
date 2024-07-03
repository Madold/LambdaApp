package com.markusw.lambda.core.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError


private const val TEST_BANNER_AD_ID = "ca-app-pub-3940256099942544/6300978111"

@Composable
fun AdmobBanner(
    modifier: Modifier = Modifier,
    onAdClick: () -> Unit = {},
    onAdLoaded: () -> Unit = {},
    onAdClosed: () -> Unit = {},
    onAdOpened: () -> Unit = {},
    onAdFailedToLoad: (LoadAdError) -> Unit = { _ -> },
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val height = 60

    AndroidView(factory = { context ->
        AdView(context).apply {
            setAdSize(AdSize(screenWidth, height))
            adUnitId = TEST_BANNER_AD_ID
            adListener = object: AdListener() {
                override fun onAdClicked() {
                    super.onAdClicked()
                    onAdClick()
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    onAdFailedToLoad(p0)
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    onAdLoaded()
                }

                override fun onAdClosed() {
                    super.onAdClosed()
                    onAdClosed()
                }

                override fun onAdOpened() {
                    super.onAdOpened()
                    onAdOpened()
                }

            }

            loadAd(AdRequest.Builder().build())
        }
    }, modifier = modifier)

}