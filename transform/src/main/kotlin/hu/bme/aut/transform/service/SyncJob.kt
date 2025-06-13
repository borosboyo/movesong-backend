package hu.bme.aut.transform.service

import hu.bme.aut.transform.repository.SyncRepository
import jakarta.transaction.Transactional
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
open class SyncJob(
    private val syncRepository: SyncRepository,
    private val transformService: TransformService
) {

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    open fun runPlaylistSyncs() {
        val now = LocalDateTime.now()

        val syncObjects = syncRepository.findAllByEnabled(true)

        syncObjects.forEach { syncObject ->
            val lastSync = syncObject.lastSyncDate
            val intervalHours = syncObject.interval

            if (ChronoUnit.HOURS.between(lastSync, now) >= intervalHours) {
                transformService.syncPlaylists(syncObject)
                syncObject.lastSyncDate = now
                syncRepository.save(syncObject)
            }
        }
    }
}