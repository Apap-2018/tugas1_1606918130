package com.apap.tugas1.service;

import com.apap.tugas1.model.PegawaiModel;

public interface PegawaiService {
	PegawaiModel getPegawaiDetailByNip(String nip);
	double getGajiLengkapByNip(String nip);
	void addPegawai(PegawaiModel pegawai);
}
