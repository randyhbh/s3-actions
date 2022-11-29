import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.deleteObjects
import aws.sdk.kotlin.services.s3.model.ListObjectsV2Request
import aws.sdk.kotlin.services.s3.model.Object
import aws.sdk.kotlin.services.s3.model.ObjectIdentifier
import aws.sdk.kotlin.services.s3.paginators.listObjectsV2Paginated
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

private const val BUCKET = ""

suspend fun performDeleteRequest(
    s3Client: S3Client,
    startSearchPrefix: String,
    dryRun: Boolean
) {
    val request = ListObjectsV2Request {
        this.delimiter = "/"
        this.prefix = startSearchPrefix
        this.bucket = BUCKET
    }

    val flowObjectResponse = s3Client.listObjectsV2Paginated(request)

    flowObjectResponse
        .onEach { println("Request page size ${it.contents?.size}") }
        .onCompletion { println("Bucket $BUCKET cleanup successfully!") }
        .mapNotNull { it.contents }
        .map { toObjectIdentifiers(it) }
        .collect { execute(dryRun, it, s3Client) }
}

private fun toObjectIdentifiers(it: List<Object>) =
    it.map { s3Object -> ObjectIdentifier { key = s3Object.key } }

private suspend fun execute(
    dryRun: Boolean,
    it: List<ObjectIdentifier>,
    s3Client: S3Client
) {
    return when {
        dryRun -> println("Dry delete of ${it.size} elements")
        else -> cleanUp(s3Client, BUCKET, it)
    }
}

private suspend fun cleanUp(s3: S3Client, bucketName: String, objectsToDelete: List<ObjectIdentifier>) {
    s3.deleteObjects {
        bucket = bucketName
        delete { objects = objectsToDelete }
    }
}

