package cl.duoc.api_eventos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public String subirTicket(String eventoId, String usuarioId, String nombreArchivo, byte[] archivoBytes) {
        String s3Key = eventoId + "/" + usuarioId + "/" + nombreArchivo;
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .contentType("application/pdf")
                .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(archivoBytes));
        return "Archivo subido exitosamente a: " + s3Key;
    }

    public byte[] descargarTicket(String eventoId, String usuarioId, String nombreArchivo) {
        String s3Key = eventoId + "/" + usuarioId + "/" + nombreArchivo;
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();
        return s3Client.getObjectAsBytes(getObjectRequest).asByteArray();
    }

    public String eliminarTicket(String eventoId, String usuarioId, String nombreArchivo) {
        String s3Key = eventoId + "/" + usuarioId + "/" + nombreArchivo;
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
        return "Archivo eliminado exitosamente de S3: " + s3Key;
    }
}