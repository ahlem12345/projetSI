package tn.esprit.ahlembensalem4cce11.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.ahlembensalem4cce11.domaine.Vehicule;
import tn.esprit.ahlembensalem4cce11.service.VehiculeServiceImpl;
@RequiredArgsConstructor
@RequestMapping("vehicules")
@RestController
public class VehiculeRestController {
    private final VehiculeServiceImpl vehiculeServices;
    public Vehicule add(Vehicule vehicule){
        return vehiculeServices.create(vehicule);


    }
    @GetMapping("get/{idVehicule}")
    public Vehicule get(@PathVariable Long idVehicule) {
        return vehiculeServices.findById(idVehicule);    }
}

