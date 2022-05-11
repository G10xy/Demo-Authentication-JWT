package it.giovi.util

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.util.FileCopyUtils
import org.springframework.web.multipart.MultipartFile
import java.io.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Objects
import java.util.Optional
import java.nio.charset.StandardCharsets.UTF_8


object Utility {
    @Throws(IOException::class)
    fun saveFile(uploadDir: String?, fileName: String, multipartFile: MultipartFile) {
        val uploadPath: Path = Paths.get(uploadDir)
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath)
        }
        try {
            multipartFile.inputStream.use { inputStream ->
                val filePath: Path = uploadPath.resolve(fileName)
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING)
            }
        } catch (ioe: IOException) {
            throw IOException("Impossibile salvare l'immagine scelta: $fileName", ioe)
        }
    }

    @Throws(IOException::class)
    fun saveFile(uploadDir: String?, fileName: String, inputStream: InputStream?) {
        val uploadPath: Path = Paths.get(uploadDir)
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath)
        }
        try {
            val filePath: Path = uploadPath.resolve(fileName)
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING)
        } catch (ioe: IOException) {
            throw IOException("Impossibile creare l'excel: $fileName", ioe)
        }
    }

    fun readFileFromFileSystem(fileLocation: String, clazz: Class<*>): String {
        var data = ""
        val classLoader = clazz.classLoader
        val file = File(Objects.requireNonNull(classLoader.getResource(fileLocation)).file)
        try {
            data = file.readText(UTF_8)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return data
    }

    fun deleteFile(filePath: String?) {
        val imageFile = File(filePath)
        if (imageFile.exists()) imageFile.delete()
    }

    @Throws(JsonProcessingException::class)
    fun <T : Any> fromJsonStringToObject(jsonString: String?, type: Class<T>): T {
        val obj: Any = ObjectMapper().readValue(jsonString, type)
        return type.cast(obj)
    }

    fun getExtensionByStringHandling(filename: String): Optional<String> {
        return Optional.ofNullable(filename)
            .filter { f -> f.contains(".") }
            .map { f -> f.substring(filename.lastIndexOf(".") + 1) }
    }

    fun createDateTime(date: LocalDate?, time: LocalTime?): LocalDateTime {
        return LocalDateTime.of(date, time)
    }

    fun createDateTime(date: LocalDate?): LocalDateTime {
        val time: LocalTime = LocalTime.MIDNIGHT
        return LocalDateTime.of(date, time)
    }

    fun getLocalDateTime(dateToConvert: Date): LocalDateTime {
        return dateToConvert.toInstant()
            .atZone(ZoneOffset.UTC)
            .toLocalDateTime()
    }

    fun getStringFromDateTime(date: LocalDateTime?, pattern: String?): String {
//        Instant instant = date.atZone(ZoneId.of("Europe/Rome")).toInstant();
        val formatter: DateTimeFormatter =
            DateTimeFormatterBuilder().appendPattern(pattern).toFormatter().withZone(ZoneOffset.UTC)
        return formatter.format(date)
    }

    fun fragmentStringOfWords(words: String, regex: String): Array<String> {
        return words.split(regex.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    }

    fun differenceBetween(first: LocalDateTime?, second: LocalDateTime?, chronoUnit: ChronoUnit): Long {
        return chronoUnit.between(first, second)
    }

    fun readResourceFile(path: String): String {
        val resourceLoader: ResourceLoader = DefaultResourceLoader()
        val resource: Resource = resourceLoader.getResource(path)
        try {
            InputStreamReader(
                resource.inputStream,
                UTF_8
            ).use { reader -> return FileCopyUtils.copyToString(reader) }
        } catch (e: IOException) {
            throw UncheckedIOException(e)
        }
    }
}