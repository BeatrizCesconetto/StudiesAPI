package br.com.cesconetto.service

import br.com.cesconetto.config.FileStorageConfig
import br.com.cesconetto.exceptions.FileStorageException
import br.com.cesconetto.exceptions.MyFileNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class FileStorageService  @Autowired constructor(fileStorageConfig: FileStorageConfig)  {

    private val fileStorageLocation: Path

    init {
        fileStorageLocation = Paths.get(fileStorageConfig.uploadDir).toAbsolutePath().normalize()
        try {
            Files.createDirectories(fileStorageLocation)
        } catch (e: Exception) {
            throw FileStorageException("Could not create the directory where the uploaded files will be stored", e)
        } }

        fun storeFile(file: MultipartFile): String {
            val fileName = StringUtils.cleanPath(file.originalFilename!!)
            return try {
                //Ver se o usuário está criando um arquivo no modelo certo
                // como, por exemplo, era para ser my_file.txt e ele bota my_file..txt
                if (fileName.contains("..")) {
                    throw FileStorageException("Sorry! Filename contains invalid path sequence $fileName")
                }
                val targetLocation = fileStorageLocation.resolve(fileName)//Para mudar para onde vai o arquivo mexe nessa linha
                Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)//Para mudar para onde vai o arquivo mexe nessa 2 linhas
                fileName
            } catch (e: Exception) {
                throw FileStorageException("Could not store file $fileName. Please try again!", e)
            }
        }

    fun loadFileAsResource(fileName: String): Resource {
        return try {
            val filePath = fileStorageLocation.resolve(fileName).normalize() //Para mudar para onde vai o arquivo mexe nessa linha
            val resource: Resource = UrlResource(filePath.toUri()) //Para mudar para onde vai o arquivo mexe nessa 2 linhas
            if (resource.exists()) {
                resource
            } else {
                throw MyFileNotFoundException("File not found $fileName")
            }
        } catch (e: Exception) {
            throw MyFileNotFoundException("File not found $fileName", e)
        }
    }

}