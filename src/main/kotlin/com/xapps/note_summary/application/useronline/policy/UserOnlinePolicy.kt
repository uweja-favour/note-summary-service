package com.xapps.note_summary.application.useronline.policy

interface UserOnlinePolicy {
    suspend fun execute(userId: String)
}