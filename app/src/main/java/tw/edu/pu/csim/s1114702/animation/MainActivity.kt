package tw.edu.pu.csim.s1114702.animation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tw.edu.pu.csim.s1114702.animation.ui.theme.AnimationTheme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.foundation.layout.Column

import androidx.compose.material3.Button
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    /*
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    */
                    Animation(m = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Animation(m: Modifier) {
    var appear by remember { mutableStateOf(true) }  //背景出現
    var expanded by remember { mutableStateOf(true) }  //背景延展
    var fly by remember { mutableStateOf(false) }  //火箭升空

    //角度動畫
    val buttonAngle by animateFloatAsState(
        if (appear) 360f else 0f,
        animationSpec = tween(durationMillis = 2500)   //用2.5秒時間旋轉角度
    )
    //顏色動畫
    val backgroundColor by animateColorAsState(
        if (appear) Color.Transparent else Color.Green,
        animationSpec = tween(2000, 500)
    )   //透明背景到綠色背景間切換
    //大小動畫
    val rocketSize by animateDpAsState(
        if (fly) 75.dp else 150.dp,
        animationSpec = tween(2000)
    )    //用2秒時間縮放大小
    //位移動畫
    val rocketOffset by animateOffsetAsState(
        if (fly) Offset(200f, -50f) else Offset(200f, 400f),
        animationSpec = tween(2000)
    )    //用2秒時間調整位置



    Column (Modifier.background(backgroundColor)){
        Button(
            onClick = { appear = !appear },

            modifier = Modifier.rotate(buttonAngle)
        ) {
            if (appear) Text(text = "星空背景圖消失")
            else Text(text = "星空背景圖出現")
        }

        AnimatedVisibility(
            visible = appear,          //背景圖片出現消失
            enter = fadeIn(         //淡入
                initialAlpha = 0.1f,
                animationSpec = tween(durationMillis = 500))
               + slideInHorizontally(   //用+來結合兩種效果
                animationSpec = tween(durationMillis = 500)) { fullWidth ->
                fullWidth / 3      //從寬度1/3往左移入
            },

            exit = fadeOut(
                animationSpec = tween(durationMillis = 500))  //淡出
                + slideOutHorizontally(
                animationSpec = tween(durationMillis = 500)) { fullWidth ->
                -fullWidth / 3     //往右移出直到寬度1/3消失
            }


        ) {


            Image(
                painter = painterResource(id = R.drawable.sky),
                contentDescription = "星空背景圖",
                    modifier = Modifier
                        .animateContentSize()
                        .fillMaxWidth()
                        .height(if (expanded) 600.dp else 400.dp)
                        .clickable(
                        ) {
                        expanded = !expanded       //點選圖片延展/收縮
                        }

            )
            Image(
                painter = painterResource(id = R.drawable.rocket),
                contentDescription = "火箭",
                modifier = Modifier
                    .size(rocketSize)   //圖片大小
                    .offset(rocketOffset.x.dp, rocketOffset.y.dp)  //圖片座標
                    .clickable(
                    ) {
                        fly = !fly
                    }
            )
        }

    }
}

