import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi

const val TIME_25_MINUTES: Int = 25 * 60
const val TIME_5_MINUTES: Int = 5 * 60

fun Int.format(length: Int): String {
    val str = this.toString()
    if (str.length > length) {
        throw Error("")
    }
    if (str.length == length) {
        return str
    }
    var remain = length - str.length
    var ret = ""
    while ((remain--) > 0) {
        ret += "0"
    }
    ret += str
    return ret
}

fun Int.toTime(): String {
    val quotient = this / 60
    val remainder = this % 60
    return "${quotient.format(2)}:${remainder.format(2)}"
}

enum class Stage {
    IDLE, // 倒计时尚未开始
    PAUSE, // 倒计时进行中，可暂停
    CONTINUE, // 可继续
    GAP // 休息
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    var stage by remember { mutableStateOf(Stage.IDLE) } // 界面
    var timeLeft by remember { mutableStateOf(TIME_25_MINUTES) } // 持有当前倒计时数据
    var isRunnable by remember { mutableStateOf(false) } // 是否正在倒计时
    var isWorking by remember { mutableStateOf(true) } // 判断是工作时间还是休息时间
    LaunchedEffect(isRunnable) {
        if (isRunnable) {
            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
            // 倒计时结束 DONE
            isWorking = !isWorking
            isRunnable = false
            stage = if (isWorking) Stage.IDLE else Stage.GAP
        }
    }
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    fontSize = if (getPlatform().isPortrait) 96.sp else 128.sp,
                    text = timeLeft.toTime(),
                    fontFamily = FontFamily.Monospace
                )
            }
            Box(modifier = Modifier.padding(48.dp), contentAlignment = Alignment.Center) {
                when (stage) {
                    Stage.IDLE -> {
                        Button(onClick = {
                            // 转换到下一阶段
                            isRunnable = true
                            timeLeft = TIME_25_MINUTES
                            isWorking = true
                            stage = Stage.PAUSE
                        }) {
                            Text(
                                text = "开始专注",
                                fontFamily = FontFamily.Serif
                            )
                        }
                    }

                    Stage.PAUSE -> {
                        Button(onClick = {
                            isRunnable = false
                            // 转换到下一阶段
                            stage = Stage.CONTINUE
                        }) {
                            Text(
                                text = "暂停",
                                fontFamily = FontFamily.Serif
                            )
                        }
                    }

                    Stage.CONTINUE -> {
                        Row {
                            Button(onClick = {
                                isRunnable = true
                                // 转换到下一阶段
                                stage = Stage.PAUSE
                            }) {
                                Text(
                                    text = "继续",
                                    fontFamily = FontFamily.Serif
                                )
                            }
                            Button(onClick = {
                                isRunnable = false
                                timeLeft = TIME_25_MINUTES
                                // 转换到下一阶段
                                stage = Stage.IDLE
                            }) {
                                Text(
                                    text = "退出",
                                    fontFamily = FontFamily.Serif
                                )
                            }
                        }
                    }

                    Stage.GAP -> {
                        Row {
                            Button(onClick = {
                                isRunnable = true
                                isWorking = false
                                timeLeft = TIME_5_MINUTES
                                // 转换到下一阶段
                                stage = Stage.PAUSE
                            }) {
                                Text(
                                    text = "开始短暂休息",
                                    fontFamily = FontFamily.Serif
                                )
                            }
                            Button(onClick = {
                                timeLeft = TIME_25_MINUTES
                                // 转换到下一阶段
                                stage = Stage.IDLE
                            }) {
                                Text(
                                    text = "跳过",
                                    fontFamily = FontFamily.Serif
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}