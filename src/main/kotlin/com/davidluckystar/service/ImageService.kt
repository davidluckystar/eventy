package com.davidluckystar.service

import com.davidluckystar.model.Event
import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * Created by david on 3/9/2017.
 */
@Component
class ImageService {

    val REPO: String = "C:\\Users\\david\\Pictures\\eventy"
    val USER_FOLDER: String = "users"
    val sdf: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
    val FILE_SEPARATOR = '@'

    fun uploadImage(creationDate: Date, filename: String, bytes: ByteArray): String {
        val containerDir = sdf.format(creationDate)
        val dirAbsolutePath = REPO + File.separator + containerDir
        val dir = Paths.get(dirAbsolutePath)
        Files.createDirectories(dir)
        val imageAbsolutePath = dirAbsolutePath + File.separator + filename
        val file = File(imageAbsolutePath)
        if (file.exists()) {
            file.delete()
        }
        File(imageAbsolutePath).writeBytes(bytes)
        val imageRelativePath = containerDir + FILE_SEPARATOR + filename
        return imageRelativePath
    }

    fun getImage(e: Event, filename: String): ByteArray {
        return Files.readAllBytes(Paths.get(REPO + File.separator + sdf.format(e.creationDate) + File.separator + filename))
    }

    fun getImage(imageRelativePath: String): ByteArray {
        val parts = imageRelativePath.split(FILE_SEPARATOR)
        return Files.readAllBytes(Paths.get(REPO + File.separator + parts[0] + File.separator + parts[1]))
    }

    fun deleteImage(imageRelativePath: String) {
        val parts = imageRelativePath.split(FILE_SEPARATOR)
        Files.deleteIfExists(Paths.get(REPO + File.separator + parts[0] + File.separator + parts[1]))
    }

    fun getUserImage(user: String): ByteArray {
        val usersFolderPath = Paths.get(REPO + File.separator + USER_FOLDER)
        Files.createDirectories(usersFolderPath)
        val listFiles = usersFolderPath.toFile().listFiles()
        val userImage = listFiles.filter { f -> Pattern.compile(user + ".*").matcher(f.name).matches() }.first()
        return Files.readAllBytes(Paths.get(userImage.absolutePath))
    }
}