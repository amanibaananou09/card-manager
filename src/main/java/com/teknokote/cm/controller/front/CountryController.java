package com.teknokote.cm.controller.front;

import com.teknokote.cm.controller.EndPoints;
import com.teknokote.cm.core.service.CountryService;
import com.teknokote.cm.dto.CountryDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping(EndPoints.COUNTRY_ROOT)
public class CountryController {
@Autowired
private CountryService countryService;


@PostMapping(EndPoints.ADD)
public ResponseEntity<CountryDto> addCountry(@RequestBody CountryDto dto) {
    CountryDto savedCountry = countryService.create(dto);
    return new ResponseEntity<>(savedCountry, HttpStatus.CREATED);
    }


    @PutMapping(EndPoints.UPDATE)
    public ResponseEntity<CountryDto> updateCountry(@RequestBody CountryDto dto) {
        CountryDto savedCountry = countryService.update(dto);
        return new ResponseEntity<>(savedCountry, HttpStatus.CREATED);
    }
    @GetMapping(EndPoints.INFO)
    public ResponseEntity<CountryDto> getCountry(@PathVariable Long id)
    {
        CountryDto foundCountry = countryService.checkedFindById(id);
        return new ResponseEntity<>(foundCountry, HttpStatus.CREATED);
    }
    @GetMapping
    public List<CountryDto> listCountry() {
        return countryService.findAll();
    }
}
