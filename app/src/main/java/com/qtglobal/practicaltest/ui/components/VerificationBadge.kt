package com.qtglobal.practicaltest.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qtglobal.practicaltest.ui.theme.PracticalTestTheme
import com.qtglobal.practicaltest.ui.theme.ErrorLight
import com.qtglobal.practicaltest.ui.theme.SuccessLight

@Composable
fun VerificationBadge(
    isVerified: Boolean,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isVerified) Icons.Default.CheckCircle else Icons.Default.AccountBox,
            contentDescription = if (isVerified) "Verified" else "Verification Failed",
            tint = if (isVerified) SuccessLight else ErrorLight,
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = if (isVerified) "✓ Verified" else "✗ Failed",
            style = MaterialTheme.typography.bodySmall,
            color = if (isVerified) SuccessLight else ErrorLight
        )
    }
}

@Preview(name = "Verified Badge", showBackground = true)
@Composable
private fun VerificationBadgeVerifiedPreview() {
    PracticalTestTheme {
        VerificationBadge(
            isVerified = true,
            label = "Body"
        )
    }
}

@Preview(name = "Failed Badge", showBackground = true)
@Composable
private fun VerificationBadgeFailedPreview() {
    PracticalTestTheme {
        VerificationBadge(
            isVerified = false,
            label = "Image"
        )
    }
}


