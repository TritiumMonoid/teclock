package com.coffeebeans.tritiummonoid.teclock.schedule

import android.content.Context
import android.util.Log
import com.coffeebeans.tritiummonoid.teclock.R
import com.github.kittinunf.fuel.httpGet
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.Exception
import java.lang.RuntimeException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class StudentScheduleNetworkProvider(
    val context: Context,
    val username: String,
    val password: String
): StudentScheduleProvider {
    val executor: ExecutorService = Executors.newSingleThreadExecutor()
    val tokenRegex = "Password=[0-9A-F]{32}".toRegex()

    override fun getStudentSchedule(): Future<StudentSchedule> {
        return executor.submit<StudentSchedule> {
            val token = login()
            Log.d("NetworkProvider", "Logged in")
            try {
                val schedule = schedule(token)
                Log.d("NetworkProvider", "Fetched data")
                schedule
            } catch (exception: Exception) {
                Log.e("NetworkProvider", "Didn't fetch data")
                throw exception
            } finally {
                logout(token)
                Log.d("NetworkProvider", "Logged out")
            }
        }
    }

    fun login(): String {
        val response = context.getString(R.string.login)
            .replace("\$USER", username)
            .replace("\$PASSWORD", password)
            .httpGet()
            .response()

        if (response.third.component2() != null) {
            throw RuntimeException()
        }

        val data = response
            .second
            .data
            .toString(charset("UTF-8"))

        val document: Document = Jsoup.parse(data)
        val src = document
            .getElementsByTag("FRAME")[0]
            .attr("SRC")

        val token = tokenRegex
            .find(src)
            ?.value
            ?.substringAfter("=")

        if (token == null) {
            throw RuntimeException()
        }

        return token
    }

    fun schedule(token: String): StudentSchedule {
        val response = context.getString(R.string.schedule)
            .replace("\$USER", username)
            .replace("\$TOKEN", token)
            .httpGet()
            .response()


        if (response.third.component2() != null) {
            throw RuntimeException()
        }

        val data = response
            .second
            .data
            .toString(charset("UTF-8"))

        val document: Document = Jsoup.parse(data)
        val rows = document.getElementsByTag("TR")
            .map { row -> row.getElementsByTag("FONT")}
            .map { row -> row.map { column -> column.text() } }
            .filter { row -> row.size == 11 }
            .drop(1)

        val week = MutableList<MutableList<StudentClass>>(7) { _ -> ArrayList()}
        rows.forEach {
            val classSeries: String = it[0]
            val className: String = it[1]
            val classCredits: String = it[2]
            val teacher: String = it[3]
            it.drop(4)
                .forEachIndexed { i, classData ->
                    val data = classData.split(" ")
                    if (data.size == 2) {
                        val classTime = data[0].split("-")
                        val start = classTime[0].replace(":", "").toInt()
                        val end = classTime[1].replace(":", "").toInt()
                        val classroom = data[1]
                        week[i].add(
                            StudentClass(classSeries, className, teacher, classroom, start, end)
                        )
                    }
                }
        }

        week.forEach { day -> day.sort() }
        return StudentSchedule(week)
    }

    fun logout(token: String) {
        val response = context.getString(R.string.logout)
            .replace("\$USER", username)
            .replace("\$TOKEN", token)
            .httpGet()
            .response()

        if (response.third.component2() != null) {
            throw RuntimeException()
        }
    }
}