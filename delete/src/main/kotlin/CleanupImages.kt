import aws.sdk.kotlin.services.s3.S3Client
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main(): Unit = runBlocking {
    val time = measureTimeMillis {
        val s3Client = S3Client.fromEnvironment {
            region = "eu-central-1"
        }

        performDeleteRequest(
            s3Client = s3Client,
            startSearchPrefix = "image/",
            dryRun = true
        )
    }
    println("Collected in $time ms")
}



