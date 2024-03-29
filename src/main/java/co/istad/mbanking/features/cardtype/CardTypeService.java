package co.istad.mbanking.features.cardtype;


import co.istad.mbanking.domain.CardType;
import co.istad.mbanking.features.cardtype.dto.CardTypeResponse;

import java.util.List;

public interface CardTypeService {

    List<CardTypeResponse> findAll();

    CardTypeResponse findByName(String name);

}
