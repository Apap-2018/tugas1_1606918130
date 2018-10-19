package com.apap.tugas1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apap.tugas1.model.PegawaiModel;

public interface PegawaiDB extends JpaRepository<PegawaiModel, Long> {
	//cari pegawai berdasarkan NIP
	PegawaiModel findByNip(String nip);
}
