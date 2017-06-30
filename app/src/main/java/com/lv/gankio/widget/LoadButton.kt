package com.lv.gankio.widget

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Looper
import android.support.v4.content.ContextCompat
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.lv.gankio.R


/**
 * Date: 2017-06-30
 * Time: 08:58
 * Description:
 */
class LoadButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr), Animator.AnimatorListener {

    private var mStrokeColor: Int
    private var mTextColor: Int
    private var mProgressWidth: Float
    private var mPaint: Paint

    private var mDefaultWidth: Int
    private var mDefaultRadiu: Int = 40

    private var rectWidth: Int = 0

    private var mTextPaint: TextPaint

    private var mDefaultTextSize: Int

    private var mTopBottomPadding: Int
    private var mLeftRightPadding: Int

    private var mText: String

    private var mTextWidth: Int = 0
    private var mTextSize: Int
    private var mRadiu: Int = 0


    private var mPath: Path? = null

    private var leftRect: RectF
    private var rightRect: RectF
    private var contentRect: RectF
    private var progressRect: RectF? = null

    private var leftM: Int = 0
    private var rightM: Int = 0
    private var topM: Int = 0
    private var bottomM: Int = 0

    private var isUnfold: Boolean = false

    private var mBackgroundColor: Int

    private var mCurrentState: State? = null
    private var circleSweep: Float = 0.toFloat()
    private var loadAnimator: ObjectAnimator? = null
    private var shrinkAnim: ObjectAnimator? = null

    private var mSuccessedDrawable: Drawable? = null
    private var mErrorDrawable: Drawable? = null
    private var mPauseDrawable: Drawable? = null

    private var progressReverse: Boolean = false
    private var mProgressSecondColor: Int = 0
    private var mProgressColor: Int = 0
    private var mProgressStartAngel: Int = 0
    private var mLoadListenner: LoadListenner? = null

    init {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadButton)
        mDefaultTextSize = 24
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.LoadButton_android_textSize, mDefaultTextSize)
        mStrokeColor = typedArray.getColor(R.styleable.LoadButton_stroke_color, Color.RED)
        mTextColor = typedArray.getColor(R.styleable.LoadButton_content_color, Color.WHITE)
        mText = typedArray.getString(R.styleable.LoadButton_android_text)
        mRadiu = typedArray.getDimensionPixelOffset(R.styleable.LoadButton_radiu, mDefaultRadiu)
        mTopBottomPadding = typedArray.getDimensionPixelOffset(R.styleable.LoadButton_contentPaddingTB, 10)
        mLeftRightPadding = typedArray.getDimensionPixelOffset(R.styleable.LoadButton_contentPaddingLR, 10)
        mBackgroundColor = typedArray.getColor(R.styleable.LoadButton_backColor, Color.WHITE)
        mProgressColor = typedArray.getColor(R.styleable.LoadButton_progressColor, Color.WHITE)
        mProgressSecondColor = typedArray.getColor(R.styleable.LoadButton_progressSecondColor, Color.parseColor("#c3c3c3"))
        mProgressWidth = typedArray.getDimensionPixelOffset(R.styleable.LoadButton_progressedWidth, 2).toFloat()

        mSuccessedDrawable = typedArray.getDrawable(R.styleable.LoadButton_loadSuccessDrawable)
        mErrorDrawable = typedArray.getDrawable(R.styleable.LoadButton_loadErrorDrawable)
        mPauseDrawable = typedArray.getDrawable(R.styleable.LoadButton_loadPauseDrawable)
        typedArray.recycle()

        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.color = mStrokeColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mProgressWidth

        mDefaultWidth = 200


        mTextPaint = TextPaint()
        mTextPaint.color = mTextColor
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.textSize = mTextSize.toFloat()


        rectWidth = mDefaultWidth - mDefaultRadiu * 2

        leftRect = RectF()
        rightRect = RectF()
        contentRect = RectF()
        isUnfold = true



        mCurrentState = State.INITIAL

        if (mSuccessedDrawable == null)
            mSuccessedDrawable = ContextCompat.getDrawable(context, R.mipmap.yes)
        if (mErrorDrawable == null)
            mErrorDrawable = ContextCompat.getDrawable(context, R.mipmap.no)
        if (mPauseDrawable == null) {
            mPauseDrawable = ContextCompat.getDrawable(context, R.mipmap.pause)
        }

        mProgressSecondColor = Color.parseColor("#c3c3c3")
        mProgressColor = Color.WHITE
    }


    fun startLoadAnim(){
        if (mCurrentState == State.FODDING)
            return

        if (mCurrentState == State.INITIAL) {
            if (isUnfold) {
                shringk()
            }
        } else if (mCurrentState == State.COMPLETED_ERROR) {

            if (mLoadListenner != null) {
                mLoadListenner!!.onClick(false)
            }


        } else if (mCurrentState == State.COMPLETED_SUCCESSED) {
            if (mLoadListenner != null) {
                mLoadListenner!!.onClick(true)
            }
        } else if (mCurrentState == State.LOADDING_PAUSE) {
            if (mLoadListenner != null) {
                mLoadListenner!!.needLoading()
                load()
            }
        } else if (mCurrentState == State.LOADDING) {
            mCurrentState = State.LOADDING_PAUSE
            cancelAnimation()
            invaidateSelft()
        }
    }



    fun setListenner(listenner: LoadListenner) {
        this.mLoadListenner = listenner
    }


    fun setCircleSweep(circleSweep: Float) {
        this.circleSweep = circleSweep
        invaidateSelft()
    }


    internal enum class State {
        INITIAL,
        FODDING,
        LOADDING,
        COMPLETED_ERROR,
        COMPLETED_SUCCESSED,
        LOADDING_PAUSE
    }


    fun reset() {
        mCurrentState = State.INITIAL
        rectWidth = width - mRadiu * 2
        isUnfold = true
        cancelAnimation()
        invaidateSelft()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)

        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        var resultW = widthSize
        var resultH = heightSize

        var contentW = 0
        var contentH = 0

        if (widthMode == View.MeasureSpec.AT_MOST) {
            mTextWidth = mTextPaint.measureText(mText).toInt()
            contentW += mTextWidth + mLeftRightPadding * 2 + mRadiu * 2

            resultW = if (contentW < widthSize) contentW else widthSize
        }

        if (heightMode == View.MeasureSpec.AT_MOST) {
            contentH += mTopBottomPadding * 2 + mTextSize
            resultH = if (contentH < heightSize) contentH else heightSize
        }

        resultW = if (resultW < 2 * mRadiu) 2 * mRadiu else resultW
        resultH = if (resultH < 2 * mRadiu) 2 * mRadiu else resultH

        mRadiu = resultH / 2
        rectWidth = resultW - 2 * mRadiu
        setMeasuredDimension(resultW, resultH)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val cx = width / 2
        val cy = height / 2

        drawPath(canvas, cx, cy)

        val textDescent = mTextPaint.fontMetrics.descent.toInt()
        val textAscent = mTextPaint.fontMetrics.ascent.toInt()
        val delta = Math.abs(textAscent) - textDescent

        val circleR = mRadiu / 2

        if (mCurrentState == State.INITIAL) {

            canvas?.drawText(mText, cx.toFloat(), (cy + delta / 2).toFloat(), mTextPaint)

        } else if (mCurrentState == State.LOADDING) {

            if (progressRect == null) {
                progressRect = RectF()
            }
            progressRect!!.set((cx - circleR).toFloat(), (cy - circleR).toFloat(), (cx + circleR).toFloat(), (cy + circleR).toFloat())



            mPaint.color = mProgressSecondColor
            canvas?.drawCircle(cx.toFloat(), cy.toFloat(), circleR.toFloat(), mPaint)
            mPaint.color = mProgressColor
            if (circleSweep != 360f) {
                mProgressStartAngel = if (progressReverse) 270 else (270 + circleSweep).toInt()
                canvas?.drawArc(progressRect, mProgressStartAngel.toFloat(), if (progressReverse) circleSweep else (360 - circleSweep),
                        false, mPaint)
            }

            mPaint.color = mBackgroundColor
        } else if (mCurrentState == State.COMPLETED_ERROR) {
            mErrorDrawable?.setBounds(cx - circleR, cy - circleR, cx + circleR, cy + circleR)
            mErrorDrawable?.draw(canvas)
        } else if (mCurrentState == State.COMPLETED_SUCCESSED) {
            mSuccessedDrawable?.setBounds(cx - circleR, cy - circleR, cx + circleR, cy + circleR)
            mSuccessedDrawable?.draw(canvas)
        } else if (mCurrentState == State.LOADDING_PAUSE) {
            mPauseDrawable?.setBounds(cx - circleR, cy - circleR, cx + circleR, cy + circleR)
            mPauseDrawable?.draw(canvas)
        }
    }

    private fun drawPath(canvas: Canvas?, cx: Int, cy: Int) {
        if (mPath == null) {
            mPath = Path()
        }

        mPath?.reset()

        leftM = cx - rectWidth / 2 - mRadiu
        topM = 0
        rightM = cx + rectWidth / 2 + mRadiu
        bottomM = height

        leftRect.set(leftM.toFloat(), topM.toFloat(), (leftM + mRadiu * 2).toFloat(), bottomM.toFloat())
        rightRect.set((rightM - mRadiu * 2).toFloat(), topM.toFloat(), rightM.toFloat(), bottomM.toFloat())
        contentRect.set((cx - rectWidth / 2).toFloat(), topM.toFloat(), (cx + rectWidth / 2).toFloat(), bottomM.toFloat())
        mPath?.moveTo((cx - rectWidth / 2).toFloat(), bottomM.toFloat())
        mPath?.arcTo(leftRect, 90.0f, 180f)
        mPath?.lineTo((cx + rectWidth / 2).toFloat(), topM.toFloat())
        mPath?.arcTo(rightRect, 270.0f, 180f)

        mPath?.close()


        mPaint.style = Paint.Style.FILL
        mPaint.color = mBackgroundColor
        canvas?.drawPath(mPath, mPaint)
        mPaint.style = Paint.Style.STROKE
        mPaint.color = mStrokeColor
    }

    fun setRectWidth(width: Int) {
        rectWidth = width
        invaidateSelft()
    }

    private fun invaidateSelft() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            invalidate()
        } else {
            postInvalidate()
        }
    }

    fun shringk() {
        if (shrinkAnim == null)
            shrinkAnim = ObjectAnimator.ofInt(this, "rectWidth", rectWidth, 0)
        shrinkAnim?.addListener(this)

        shrinkAnim?.duration = 500
        shrinkAnim?.start()
        mCurrentState = State.FODDING
    }

    fun load() {
        if (loadAnimator == null) {
            loadAnimator = ObjectAnimator.ofFloat(this, "circleSweep", 0f, 360f)
        }

        loadAnimator?.duration = 1000
        loadAnimator?.repeatMode = ValueAnimator.RESTART
        loadAnimator?.repeatCount = ValueAnimator.INFINITE

        loadAnimator?.removeAllListeners()

        loadAnimator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {

            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {
                //                Log.d(TAG,"onAnimationRepeat:"+progressReverse);
                progressReverse = !progressReverse
            }
        })
        loadAnimator?.start()
        mCurrentState = State.LOADDING
    }

    fun loadSuccessed() {
        mCurrentState = State.COMPLETED_SUCCESSED
        cancelAnimation()
        invaidateSelft()
    }

    fun loadFailed() {
        mCurrentState = State.COMPLETED_ERROR
        cancelAnimation()
        invaidateSelft()
    }

    override fun onAnimationStart(animation: Animator) {

    }

    override fun onAnimationEnd(animation: Animator) {
        isUnfold = false
        load()
    }

    override fun onAnimationCancel(animation: Animator) {

    }

    override fun onAnimationRepeat(animation: Animator) {

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        cancelAnimation()

    }

    private fun cancelAnimation() {
        if (shrinkAnim != null && shrinkAnim!!.isRunning) {
            shrinkAnim?.removeAllListeners()
            shrinkAnim?.cancel()
            shrinkAnim = null
        }
        if (loadAnimator != null && loadAnimator!!.isRunning) {
            loadAnimator?.removeAllListeners()
            loadAnimator?.cancel()
            loadAnimator = null
        }
    }

    interface LoadListenner {

        fun onClick(isSuccessed: Boolean)

        fun needLoading()
    }


}