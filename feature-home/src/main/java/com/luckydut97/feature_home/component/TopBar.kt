package com.luckydut97.feature_home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luckydut97.lighton.feature.home.R

@Composable
fun TopBar(
    onSearchClick: () -> Unit = {},
    onAlarmClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_typo_black),
                contentDescription = "Logo",
                modifier = Modifier.padding(start = 17.dp)
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 17.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_alarm),
                    contentDescription = "Notifications",
                    modifier = Modifier
                        .size(27.dp)
                        .clickable { onAlarmClick() }
                )
                Spacer(modifier = Modifier.width(9.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search",
                    modifier = Modifier
                        .size(27.dp)
                        .clickable { onSearchClick() }
                )
            }
        }
    }
}

@Preview
@Composable
fun TopBarPreview() {
    TopBar()
}