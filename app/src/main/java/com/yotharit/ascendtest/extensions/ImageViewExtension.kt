package com.yotharit.ascendtest.extensions

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.yotharit.ascendtest.R


fun ImageView.loadFromUrl(url: String) =
	Glide.with(this.context.applicationContext).load(url)
		.placeholder(R.drawable.ic_image_placeholder)
        .into(this)

fun ImageView.loadSvgFromUrl(url: String) =
	GlideToVectorYou.init().with(this.context.applicationContext).load(Uri.parse(url), this)