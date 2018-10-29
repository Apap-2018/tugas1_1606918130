package com.apap.tugas1.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.model.ProvinsiModel;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.ProvinsiService;


@Controller
public class PegawaiController {
	@Autowired
	private PegawaiService pegawaiService;

	@Autowired
	private ProvinsiService provinsiService;
	
	@Autowired
	private InstansiService instansiService;
	
	@Autowired 
	private JabatanService jabatanService;

	@RequestMapping("/")
	private String index(Model model) {
		model.addAttribute("listJabatan",jabatanService.findAllJabatan());
		model.addAttribute("listInstansi",instansiService.findAllInstansi());
		return "index";
	}

	//mendapatkan obyek pegawai (fitur 1)
	@RequestMapping(value = "/pegawai")
	public String viewPegawai(@RequestParam("nip") String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiDetailByNip(nip);

		model.addAttribute("pegawai", pegawai);
		model.addAttribute("gajiLengkap", Math.round(pegawaiService.getGajiLengkapByNip(nip)));
		model.addAttribute("jabatanList", pegawai.getJabatanList());
		
		return "view-pegawai";
		
	}	

	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.GET)
	private String addPegawai(Model model) {
		PegawaiModel pegawai = new PegawaiModel();
		pegawai.setInstansi(new InstansiModel());
		
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("listProvinsi", provinsiService.getProvinsiList());
		model.addAttribute("listJabatan", jabatanService.findAllJabatan());
		
		return "tambah-pegawai";
	}

	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST)
	private String addPegwawaiSubmit(@ModelAttribute PegawaiModel pegawai, Model model) {
		String nip = "";
		
		nip += pegawai.getInstansi().getId();
		
		String[] tglLahir = pegawai.getTanggalLahir().toString().split("-");
		String tglLahirString = tglLahir[2] + tglLahir[1] + tglLahir[0].substring(2, 4);
		nip += tglLahirString;

		nip += pegawai.getTahunMasuk();

		int counterSama = 1;
		for (PegawaiModel pegawaiInstansi:pegawai.getInstansi().getPegawaiInstansi()) {
			if (pegawaiInstansi.getTahunMasuk().equals(pegawai.getTahunMasuk()) && pegawaiInstansi.getTanggalLahir().equals(pegawai.getTanggalLahir())) {
				counterSama += 1;
			}	
		}
		nip += "0" + counterSama;

		for (JabatanModel jabatan:pegawai.getJabatanList()) {
			System.out.println(jabatan.getNama());
		}
		pegawai.setNip(nip);
		pegawaiService.addPegawai(pegawai);
		model.addAttribute("pegawai", pegawai);
		return "sukses-add-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/ubah")
	public String changePegawai(@RequestParam("nip") String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiDetailByNip(nip);
		
		model.addAttribute("listProvinsi", provinsiService.getProvinsiList());
		model.addAttribute("listJabatan", jabatanService.findAllJabatan());
		model.addAttribute("pegawai", pegawai);
		return "change-pegawai";	
	}	
	
	@RequestMapping(value = "/pegawai/ubah", method = RequestMethod.POST)
	private String ubahPegawaiSubmit(@ModelAttribute PegawaiModel pegawai, Model model) {
		String nip = "";
		
		nip += pegawai.getInstansi().getId();
		
		String[] tglLahir = pegawai.getTanggalLahir().toString().split("-");
		String tglLahirString = tglLahir[2] + tglLahir[1] + tglLahir[0].substring(2, 4);
		nip += tglLahirString;
		
		nip += pegawai.getTahunMasuk();
		
		int counterSama = 1;
		for (PegawaiModel pegawaiInstansi:pegawai.getInstansi().getPegawaiInstansi()) {
			if (pegawaiInstansi.getTahunMasuk().equals(pegawai.getTahunMasuk()) && pegawaiInstansi.getTanggalLahir().equals(pegawai.getTanggalLahir()) && pegawaiInstansi.getId() != pegawai.getId()) {
				counterSama += 1;
			}	
		}
		nip += "0" + counterSama;

		for (JabatanModel jabatan:pegawai.getJabatanList()) {
			System.out.println(jabatan.getNama());
		}
		pegawai.setNip(nip);
		pegawaiService.addPegawai(pegawai);
		model.addAttribute("pegawai", pegawai);
		return "sukses-change-pegawai";
	}
	
	//membandingkan umur pegawai dari instansi (fitur 10)
	@RequestMapping("/pegawai/termuda-tertua")
	private String viewTermudaTertua(@RequestParam("idInstansi") long id, Model model) {
		InstansiModel instansi = instansiService.getInstansiById(id);
		List<PegawaiModel> listPegawaiInstansi = instansi.getPegawaiInstansi();
		
		PegawaiModel pegawaiMuda;
		PegawaiModel pegawaiTua;
		
		if(listPegawaiInstansi.size()>0) {
			pegawaiMuda = listPegawaiInstansi.get(1);
			pegawaiTua = listPegawaiInstansi.get(1);
		
			for (PegawaiModel pegawaiTarget : listPegawaiInstansi) {
				Date tanggalTarget = pegawaiTarget.getTanggalLahir();
				if(tanggalTarget.before(pegawaiTua.getTanggalLahir())) {
					pegawaiTua = pegawaiTarget;
				} else if(tanggalTarget.after(pegawaiMuda.getTanggalLahir())) {
					pegawaiMuda = pegawaiTarget;
				}
			}
			
			model.addAttribute("pegawaiMuda", pegawaiMuda);
			model.addAttribute("pegawaiTua", pegawaiTua);
			return "view-termuda-tertua";
		}
				
		return "response";
	}
	
	@RequestMapping(value = "/pegawai/cari")
	public String viewPegawaiFilter(Model model) {
		List<ProvinsiModel> listProvinsi = provinsiService.getProvinsiList();
		model.addAttribute("listProvinsi", listProvinsi);
		
		List<JabatanModel> listJabatan = jabatanService.findAllJabatan();
		model.addAttribute("listJabatan", listJabatan);
		
		InstansiModel instansi = new InstansiModel();
		model.addAttribute("instansi", instansi);
		
		return "view-pegawai-filter";
	}
	
	@RequestMapping(value = "/pegawai/cari",  params = {"idProvinsi", "idInstansi", "idJabatan"})
	private String viewPegawaiFilterFix(@RequestParam(value = "idProvinsi") long idProvinsi,
							   		 @RequestParam(value = "idInstansi") long idInstansi,
							   		 @RequestParam(value = "idJabatan") long idJabatan,
							   		 Model model) {
		
		InstansiModel instansiTemp = instansiService.getInstansiById(idInstansi);
		JabatanModel jabatan = jabatanService.getJabatanDetailById(idJabatan);
		
		List<PegawaiModel> listPegawai = instansiTemp.getPegawaiInstansi();
		List<PegawaiModel> listPegawaiFix = new ArrayList<>();
		for (PegawaiModel pegawai : listPegawai) {
			for (JabatanModel jabatanPegawai : pegawai.getJabatanList()) {
				if (jabatanPegawai.equals(jabatan)) {
					listPegawaiFix.add(pegawai);
				}
			}
		}
		
		model.addAttribute("listPegawaiFix", listPegawaiFix);
		
		List<ProvinsiModel> listProvinsi = provinsiService.getProvinsiList();
		model.addAttribute("listProvinsi", listProvinsi);
		
		List<JabatanModel> listJabatan = jabatanService.findAllJabatan();
		model.addAttribute("listJabatan", listJabatan);
		
		InstansiModel instansi = new InstansiModel();
		model.addAttribute("instansi", instansi);
		
		return "view-pegawai-filter";
	}
	
}
	
