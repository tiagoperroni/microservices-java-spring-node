package com.tiagoperroni.client.mapper;

import com.tiagoperroni.client.model.AdressRequest;
import com.tiagoperroni.client.model.AdressResponse;

import org.springframework.beans.BeanUtils;

import lombok.Data;

@Data
public class AdressMapper {
    
    public static AdressResponse convert(AdressRequest adressRequest, String numero, String complemento) {
        var adressResponse = new AdressResponse();
        BeanUtils.copyProperties(adressRequest, adressResponse);
        adressResponse.setCidade(adressRequest.getLocalidade());
        adressResponse.setRua(adressRequest.getLogradouro());
        adressResponse.setNumero(numero);
        adressResponse.setComplemento(complemento);
        return adressResponse;
    }
}
