package com.apap.tugas1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.service.JabatanPegawaiService;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.ProvinsiService;


@Controller
public class JabatanController {
	@Autowired
	private PegawaiService pegawaiService;

	@Autowired
	private ProvinsiService provinsiService;
	
	@Autowired 
	private JabatanService jabatanService;
	
	@Autowired 
	private JabatanPegawaiService jabatanPegawaiService;

	//untuk menambah jabatan baru (fitur 5)
	@RequestMapping(value = "/jabatan/tambah", method = RequestMethod.GET)
	private String addJabatan(Model model) {
		JabatanModel jabatan = new JabatanModel();
		
		model.addAttribute("jabatan", jabatan);
		return "tambah-jabatan";
	}

	@RequestMapping(value = "/jabatan/tambah", method = RequestMethod.POST)
	private String addJabatanSubmit(@ModelAttribute JabatanModel jabatan, Model model) {
		jabatanService.addJabatan(jabatan);
		model.addAttribute("jabatan", jabatan);
		return "sukses-add-jabatan";
	}
	
	//untuk menampilkan data suatu jabatan (fitur 6)
	@RequestMapping(value = "/jabatan/view", method = RequestMethod.GET)
	public String viewJabatan(@RequestParam("idJabatan") long idJabatan, Model model) {
		JabatanModel jabatan = jabatanService.getJabatanDetailById(idJabatan);

		model.addAttribute("jabatan", jabatan);
		return "view-jabatan";
	}
	
	//untuk mengubah data jabatan (fitur 7)
	@RequestMapping(value = "/jabatan/ubah", method = RequestMethod.GET)
	public String ubahJabatan(@RequestParam("idJabatan") long idJabatan, Model model) {
		JabatanModel jabatan = jabatanService.getJabatanDetailById(idJabatan);
		
		model.addAttribute("jabatan", jabatan);
		return "change-jabatan";
	}
	
	//untuk menampilkan halaman sukses mengubah data jabatan (fitur 7)
	@RequestMapping(value = "/jabatan/ubah", method = RequestMethod.POST)
	public String ubahJabatanSubmit(@ModelAttribute JabatanModel jabatan, Model model) {
		
		jabatanService.addJabatan(jabatan);
		model.addAttribute("jabatan", jabatan);
		return "sukses-change-jabatan";
	}
	
	//untuk hapus jabatan (fitur 8)
	@RequestMapping(value = "/jabatan/hapus", method = RequestMethod.POST)
	public String hapusJabatanSubmit(@ModelAttribute JabatanModel jabatan, Model model) {
		JabatanModel jabatanDelete = jabatanService.getJabatanDetailById(jabatan.getId());
		model.addAttribute("jabatan", jabatanDelete);
		if (jabatanDelete.jabatanSize() != 0) {
			return "gagal-delete-jabatan";
		}
		else {
			jabatanService.deleteJabatan(jabatanDelete);
			return "sukses-delete-jabatan";
		}
	}
	
	//untuk menampilkan semua jabatan (untuk fitur 9)
	@RequestMapping(value = "/jabatan/viewall", method = RequestMethod.GET)
	public String lihatSemuaJabatan(Model model) {
		List<JabatanModel> listJabatan = jabatanService.findAllJabatan();
		for (JabatanModel jabatan:listJabatan) {
			jabatan.setSizePegawai(jabatan.jabatanSize());
		}
		model.addAttribute("listJabatan", listJabatan);
		return "view-all-jabatan";
	}
	
}