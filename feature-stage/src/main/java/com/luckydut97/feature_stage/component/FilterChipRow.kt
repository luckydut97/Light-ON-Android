package com.luckydut97.feature_stage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.feature_stage.model.FilterChip
import com.luckydut97.lighton.core.ui.theme.PretendardFamily

@Composable
fun FilterChipRow(
    chips: List<FilterChip>,
    onChipClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val assistive = Color(0xFFC4C4C4) // AssistiveColor

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .background(Color.White)
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(start = 18.dp, end = 18.dp)
        ) {
            items(chips) { chip ->
                FilterChipItem(
                    chip = chip,
                    onClick = { onChipClick(chip.id) }
                )
            }
        }
    }
}

@Composable
fun FilterChipItem(
    chip: FilterChip,
    onClick: () -> Unit
) {
    val assistive = Color(0xFFC4C4C4)

    Box(
        modifier = Modifier
            .height(30.dp)
            .clip(RoundedCornerShape(16.dp))
            .then(
                if (chip.isSelected) {
                    Modifier.background(Color.Black)
                } else {
                    Modifier
                        .background(Color.White)
                        .border(1.dp, assistive, RoundedCornerShape(16.dp))
                }
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = chip.name,
            color = if (chip.isSelected) Color.White else assistive,
            fontSize = 15.sp,
            fontWeight = if (chip.isSelected) FontWeight.Bold else FontWeight.Normal,
            fontFamily = PretendardFamily,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}
