import aws.sdk.kotlin.services.s3.S3Client
import kotlinx.coroutines.runBlocking

private val documentFolders = listOf(
    ""
)

fun main(args: Array<String>): Unit = runBlocking {

    val s3Client = S3Client.fromEnvironment {
        region = "eu-central-1"
    }

    performDeleteRequest(
        s3Client = s3Client,
        startSearchPrefix = "document/",
        dryRun = true
    )
}