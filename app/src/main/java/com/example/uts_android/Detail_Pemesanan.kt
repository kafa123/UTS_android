package com.example.uts_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.uts_android.databinding.ActivityDetailPemesananBinding
import java.sql.Time

class Detail_Pemesanan : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPemesananBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailPemesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title=intent.getStringExtra("Title")
        val Date=intent.getStringExtra(Pemesanan.EXTRA_DATE)
        val Time=intent.getStringExtra(Pemesanan.EXTRA_TIME)
        val Seat=intent.getStringExtra(Pemesanan.EXTRA_SEAT)
        val Bioskop=intent.getStringExtra(Pemesanan.EXTRA_BIOSKOP)
        val Fee=intent.getStringExtra(Pemesanan.EXTRA_FEE)
        val TotalPayment=intent.getStringExtra(Pemesanan.EXTRA_TOTAL_PAYMENT)
        val NumberOfSeat=intent.getStringExtra(Pemesanan.EXTRA_NUMBER_OF_SEAT)
        val PaymentMethod=intent.getStringExtra(Pemesanan.EXTRA_METHOD)

        with(binding){
            titleMovie.text=title
            tvBioskop.text=Bioskop
            dateMovie.text=Date+" "+Time
            seat.text=Seat
            payment.text=Fee
            tvNumberOfSeat.text=NumberOfSeat
            totalPayment.text=TotalPayment
            paymentMethod.text=PaymentMethod
        }
    }
}