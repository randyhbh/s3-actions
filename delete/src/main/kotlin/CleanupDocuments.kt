import aws.sdk.kotlin.services.s3.S3Client
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {

    val s3Client = S3Client.fromEnvironment {
        region = "eu-central-1"
    }

    performDeleteRequest(
        s3Client = s3Client,
        startSearchPrefix = "document/",
        dryRun = false,
        bucketName = BucketName("")
    )
}