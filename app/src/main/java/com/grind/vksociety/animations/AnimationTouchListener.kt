package com.grind.vksociety.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.OvershootInterpolator
import com.google.android.material.animation.AnimatorSetCompat
import com.grind.vksociety.R
import kotlinx.coroutines.*

import kotlin.coroutines.CoroutineContext

class AnimationTouchListener: View.OnTouchListener {
    companion object{
        private const val UNDEFINED_CLICK_TYPE = 0
        private const val LONG_CLICK_TYPE = 1
        private const val COMMON_CLICK_TYPE = 2
    }
    private var clickType = UNDEFINED_CLICK_TYPE
    private var currView: View? = null
    private var checkLongClickJob = newInstanceLongClickJob()

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        currView = v
        val logo = v?.findViewById<View>(R.id.cimv_logo)
        val checkFrame = v?.findViewById<View>(R.id.cl_check_frame)
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                clickType = UNDEFINED_CLICK_TYPE
                checkLongClickJob.start()
                actionDownAnimatorSet(logo).start()
                actionDownAnimatorSet(checkFrame).start()
            }
            MotionEvent.ACTION_UP -> {
                checkLongClickJob.cancel("it's common click")
                checkLongClickJob = newInstanceLongClickJob()
                if(clickType != LONG_CLICK_TYPE){
                    clickType = COMMON_CLICK_TYPE
                    v?.performClick()
                }
                actionUpAnimatorSet(logo).start()
                actionUpAnimatorSet(checkFrame).start()
            }

            MotionEvent.ACTION_MOVE -> return false

            MotionEvent.ACTION_CANCEL -> {
                checkLongClickJob.cancel("it's common click")
                checkLongClickJob = newInstanceLongClickJob()
                actionUpAnimatorSet(logo).start()
                actionUpAnimatorSet(checkFrame).start()
            }
        }
        return true
    }

    private fun newInstanceLongClickJob(): Job = CoroutineScope(Dispatchers.Main).launch(start = CoroutineStart.LAZY){
        delay(ViewConfiguration.getLongPressTimeout().toLong())
        clickType = LONG_CLICK_TYPE
        currView?.performLongClick()
        checkLongClickJob = newInstanceLongClickJob()
        actionUpAnimatorSet(currView?.findViewById<View>(R.id.cimv_logo)).start()
        actionUpAnimatorSet(currView?.findViewById<View>(R.id.cl_check_frame)).start()
    }

    private fun actionDownAnimatorSet(v: View?): AnimatorSet{
        val scaleX = ObjectAnimator.ofFloat(v, "scaleX", 0.9f).apply {
            duration = 250
            interpolator = OvershootInterpolator()
        }
        val scaleY = ObjectAnimator.ofFloat(v, "scaleY", 0.9f).apply {
            duration = 250
            interpolator = OvershootInterpolator()
        }
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX,scaleY)
        return animatorSet
    }

    private fun actionUpAnimatorSet(v: View?): AnimatorSet{
        val scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1f).apply {
            duration = 250
            interpolator = OvershootInterpolator()
        }
        val scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1f).apply {
            duration = 250
            interpolator = OvershootInterpolator()
        }
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX,scaleY)
        return animatorSet
    }
}