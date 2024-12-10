package br.com.beatriz.controller

import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import br.com.beatriz.data.vo.v1.UploadFileResponseVO
import br.com.beatriz.service.FileStorageService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.logging.Logger

@Tag(name = "File Endpoint")
@RestController
@RequestMapping("/api/file/v1")
class FileController {

    private val logger = Logger.getLogger(FileController::class.java.name)

    @Autowired
    private lateinit var fileStorageService: FileStorageService

    @PostMapping("/uploadFile")
    fun uploadFile(@RequestParam("file") file: MultipartFile): UploadFileResponseVO {
        val fileName = fileStorageService.storeFile(file)
        val fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath() //Vai pegar o caminho padrão localhost 8080 e vai concatenar com o resto
            .path("/api/file/v1/uploadFile/")
            .path(fileName)
            .toUriString()

        return UploadFileResponseVO(fileName, fileDownloadUri, file.contentType!!, file.size)
    }
    @PostMapping("/uploadMultipleFile")
    fun uploadMultipleFile(@RequestParam("files") files: Array<MultipartFile>): List<UploadFileResponseVO> {
        val uploadFileResponseVOs = arrayListOf<UploadFileResponseVO>()
        for (file in files) {
           var uploadFileResponseVO: UploadFileResponseVO = uploadFile(file)
           uploadFileResponseVOs.add(uploadFileResponseVO)
        }
        return uploadFileResponseVOs
    }

    @GetMapping("/downloadFile/{fileName:.+}") //:.+ -> porque depende da extenção do arquivo for .png, .pdf e etc por isso colocamos isso
    fun downloadFile(@PathVariable fileName: String, request: HttpServletRequest): ResponseEntity<Resource> {
        val resource = fileStorageService.loadFileAsResource(fileName)
        var contentType = ""
        try {
            contentType = request.servletContext.getMimeType(resource.file.absolutePath)
        } catch (e: Exception) {
            logger.info("Could not determine file type!")
        }
        if (contentType.isBlank()) {
            contentType = "application/octet-stream"
        }
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, """attachment; filename="${resource.filename}"""")
            .body(resource)
    }
}