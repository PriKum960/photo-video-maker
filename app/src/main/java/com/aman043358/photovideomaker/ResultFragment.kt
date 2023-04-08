package com.aman043358.photovideomaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.aman043358.photovideomaker.databinding.FragmentResultBinding
import com.aman043358.photovideomaker.utils.Utils
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import com.arthenica.ffmpegkit.Statistics
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.IOException
import java.math.BigDecimal

class ResultFragment : Fragment() {

    lateinit var binding: FragmentResultBinding
    var statistics: Statistics? = null
    var videoPath: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        try {

            statistics = null
            val command = createCommand(Utils.images.map { it.path!! }.toTypedArray())

            FFmpegKit.executeAsync(command, { sessionComplete ->

                val returnCode = sessionComplete.returnCode

                requireActivity().runOnUiThread {
                    if (ReturnCode.isSuccess(returnCode)) {
                        videoPath?.let { path ->
                            binding.progressContainer.visibility = View.GONE
                            binding.videoItem.visibility = View.VISIBLE
                            Glide.with(binding.root)
                                .load(path)
                                .apply(RequestOptions().centerCrop())
                                .into(binding.iv)

                            binding.tv.text = path.split("/").last()
                            binding.iv.setOnClickListener {
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.setDataAndType(Uri.parse(path), "video/*")
                                ContextCompat.startActivity(
                                    binding.root.context,
                                    Intent.createChooser(intent, "play video using"),
                                    null
                                )
                            }
                        }
                    } else {
                        Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show()
                    }

                }

            }, {

            }) { statistic ->
                statistics = statistic

                requireActivity().runOnUiThread {
                    if (statistics!!.time < 0) {
                        return@runOnUiThread
                    }

                    val timeInMilliseconds = this.statistics!!.time
                    val totalVideoDuration = 3000 * Utils.images.size

                    val completePercentage =
                        BigDecimal(timeInMilliseconds).multiply(BigDecimal(100)).divide(
                            BigDecimal(totalVideoDuration), 0, BigDecimal.ROUND_HALF_UP
                        )
                            .toString()
                    binding.pb.isIndeterminate = false
                    binding.pb.progress = completePercentage.toInt()
                    binding.tvPercentage.text = completePercentage

                }
            }

        } catch (e: IOException) {
            Toast.makeText(
                requireContext(),
                "Encode video failed ${e.stackTrace}.",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }


    fun createCommand(arrPath: Array<String>): String {
        videoPath = "/storage/emulated/0/Test/${SystemClock.currentThreadTimeMillis()}.mp4"
        var command = "-hide_banner -y "
        val filter1 =
            "loop=loop=-1:size=1:start=0,setpts=PTS-STARTPTS,scale=w='if(gte(iw/ih,640/427),min(iw,640),-1)':h='if(gte(iw/ih,640/427),-1,min(ih,427))',scale=trunc(iw/2)*2:trunc(ih/2)*2,setsar=sar=1/1,split=2"

        command += "-i " + arrPath[0] + " "


        for (i in 1..arrPath.lastIndex)
            command += "-i " + arrPath[i] + " "

        command += "-filter_complex \""

        for (i in arrPath.indices) {
            command += "[$i:v]" + filter1 + "[stream${i + 1}out1][stream${i + 1}out2];"
        }


        command += "[stream1out1]pad=width=640:height=427:x=(640-iw)/2:y=(427-ih)/2:color=#00000000,trim=duration=3,select=lte(n\\,90)[stream1overlaid];"
        command += "[stream1out2]pad=width=640:height=427:x=(640-iw)/2:y=(427-ih)/2:color=#00000000,trim=duration=1,select=lte(n\\,30)[stream1ending];"

        for (i in 2..arrPath.lastIndex) {
            command += "[stream${i}out1]pad=width=640:height=427:x=(640-iw)/2:y=(427-ih)/2:color=#00000000,trim=duration=2,select=lte(n\\,60)[stream${i}overlaid];"
            command += "[stream${i}out2]pad=width=640:height=427:x=(640-iw)/2:y=(427-ih)/2:color=#00000000,trim=duration=1,select=lte(n\\,30),split=2[stream${i}starting][stream${i}ending];"
        }

        command += "[stream${arrPath.size}out1]pad=width=640:height=427:x=(640-iw)/2:y=(427-ih)/2:color=#00000000,trim=duration=2,select=lte(n\\,60)[stream${arrPath.size}overlaid];"
        command += "[stream${arrPath.size}out2]pad=width=640:height=427:x=(640-iw)/2:y=(427-ih)/2:color=#00000000,trim=duration=1,select=lte(n\\,30)[stream${arrPath.size}starting];"

        for (i in 1..arrPath.lastIndex) {
            command += "[stream${i + 1}starting][stream${i}ending]blend=all_expr=\'if(gte(X,(W/2)*T/1)*lte(X,W-(W/2)*T/1),B,A)\':shortest=1[stream${i + 1}blended];"
        }

        for (i in 1..arrPath.lastIndex) {
            command += "[stream${i}overlaid][stream${i + 1}blended]"
        }

        command += "[stream${arrPath.size}overlaid]"

        val n = 2 * arrPath.size - 1
        command += "concat=n=${n}:v=1:a=0,scale=w=640:h=424,format=yuv420p[video]\""

        command += " -map [video] -vsync 2 -async 1 -c:v mpeg4 -r 30 " + videoPath



        return command
    }
}