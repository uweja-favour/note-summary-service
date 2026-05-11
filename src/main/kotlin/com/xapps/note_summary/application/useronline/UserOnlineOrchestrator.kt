package com.xapps.note_summary.application.useronline

import com.xapps.note_summary.application.useronline.policy.UserOnlinePolicy
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component

@Component
class UserOnlineOrchestrator(
    private val policies: List<UserOnlinePolicy>
) {

    suspend fun handleUserOnline(userId: String) = coroutineScope {
        for (policy in policies) {
            launch {
                policy.execute(userId)
            }
        }
    }
}