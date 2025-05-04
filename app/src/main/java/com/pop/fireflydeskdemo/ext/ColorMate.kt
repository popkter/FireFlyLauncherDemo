package com.pop.fireflydeskdemo.ext

import androidx.compose.ui.graphics.Color
import com.pop.fireflydeskdemo.ui.theme.BlueSea
import com.pop.fireflydeskdemo.ui.theme.BlueSky
import com.pop.fireflydeskdemo.ui.theme.CloudyGray
import com.pop.fireflydeskdemo.ui.theme.DarkLoam
import com.pop.fireflydeskdemo.ui.theme.FoggyBlueGray
import com.pop.fireflydeskdemo.ui.theme.Grape
import com.pop.fireflydeskdemo.ui.theme.GraySky
import com.pop.fireflydeskdemo.ui.theme.Lime
import com.pop.fireflydeskdemo.ui.theme.PartlyCloudyWhite
import com.pop.fireflydeskdemo.ui.theme.PureWhite
import com.pop.fireflydeskdemo.ui.theme.RainyBlue
import com.pop.fireflydeskdemo.ui.theme.SnowyWhite
import com.pop.fireflydeskdemo.ui.theme.SunnyGold
import com.pop.fireflydeskdemo.ui.theme.WindyGray
import com.pop.fireflydeskdemo.vm.DateViewModel.Companion.DATE_TIME
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.CLEAR_DAY
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.CLEAR_NIGHT
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.CLOUDY
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.FOG
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.PARTLY_CLOUDY_DAY
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.PARTLY_CLOUDY_NIGHT
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.RAIN
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.SNOW
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.WIND


sealed class ColorMate(
    val key: String,
    val primaryColor: Color = Lime,
    val secondaryColor: Color = BlueSky,
    val tertiaryColor: Color = Grape,
    val primaryBackgroundColor: Color = PureWhite,
    val secondaryBackgroundColor: Color = GraySky
) {
    data object Snow : ColorMate(SNOW, BlueSky, SnowyWhite, primaryBackgroundColor = Grape)

    data object Rain : ColorMate(RAIN, PureWhite, RainyBlue, primaryBackgroundColor = Grape)

    data object Fog : ColorMate(FOG, DarkLoam, FoggyBlueGray, primaryBackgroundColor = Grape)

    data object Wind : ColorMate(WIND, BlueSky, WindyGray, primaryBackgroundColor = Grape)

    data object Cloudy : ColorMate(CLOUDY, PureWhite, CloudyGray, primaryBackgroundColor = Grape)

    data object PartlyCloudy :
        ColorMate(PARTLY_CLOUDY_DAY, BlueSky, PartlyCloudyWhite, primaryBackgroundColor = Grape)

    data object PartlyCloudyNight :
        ColorMate(PARTLY_CLOUDY_NIGHT, BlueSky, PartlyCloudyWhite, primaryBackgroundColor = Grape)

    data object ClearDay :
        ColorMate(CLEAR_DAY, PureWhite, SunnyGold, primaryBackgroundColor = Grape)

    data object ClearNight :
        ColorMate(CLEAR_NIGHT, PureWhite, SunnyGold, primaryBackgroundColor = Grape)

    data object DateTime :
        ColorMate(
            DATE_TIME,
            SunnyGold,
            Lime,
            DarkLoam,
            primaryBackgroundColor = BlueSky,
            secondaryBackgroundColor = BlueSea
        )

    companion object {
        val all: List<ColorMate> by lazy {
            ColorMate::class.sealedSubclasses.mapNotNull { it.objectInstance }
        }

        fun fromKey(key: String): ColorMate =
            all.firstOrNull { it.key == key } ?: error("No ColorMate found for key: $key")

    }
}
