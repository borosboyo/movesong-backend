package hu.bme.aut.user

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.io.File
import java.time.LocalDate

@Service
class LoggingJob {

    /**
     * This job runs every day at midnight.
     * It moves the current log file to a new file with the accurate date in the filename.
     * It also creates a new empty log file to replace the current one.
     */
    @Scheduled(cron = "0 0 0 * * *")
    fun deleteFilesScheduledTask() {
        println("Moving log files...")
        val logFolderPath = "./src/main/resources/logs"
        val logFileName = "user.log"

        // Get the current date and time
        val currentDate = LocalDate.now()

        // Create a new log file with the accurate date in the filename
        val newLogFile = File("$logFolderPath/$logFileName-${currentDate.minusDays(1)}.log")
        newLogFile.createNewFile()

        // Get the current log file
        val currentLogFile = File("$logFolderPath/$logFileName")

        // Rename the current log file to the accurate date
        if (currentLogFile.exists()) {
            currentLogFile.renameTo(newLogFile)
        }

        // Create a new empty log file to replace the current one
        currentLogFile.createNewFile()

        println("Log files moved successfully.")
    }
}