package com.crossover.ivolunteer.business.service;

import com.crossover.ivolunteer.business.entity.Ong;
import com.crossover.ivolunteer.data.repository.OngRepository;
import com.crossover.ivolunteer.presentation.dto.OngDto;
import com.crossover.ivolunteer.util.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@Transactional
public class OngService extends EntityServiceBase<Ong, Long, OngRepository> {

    static class OngComparatorBySeguidores implements Comparator<Ong> {
        public int compare(Ong c1, Ong c2) {
            return c1.getSeguidores().size() - c2.getSeguidores().size();
        }
    }

    public List<OngDto> getAllDtoFiltered(String nome,
                                          String[] areas) {
        // TODO: Add pagination
        Stream<Ong> ongs = findAll().stream();
        if (nome != null && nome.length() > 0) {
            String finalNome = nome.toLowerCase();
            ongs = ongs.filter(o -> o.getNome().toLowerCase().contains(finalNome));
        }
        if (areas != null && areas.length > 0) {
            ongs = ongs.filter(o -> ArrayUtils.containsAny(o.getAreas().toArray(), areas));
        }
        ongs = ongs.sorted(new OngComparatorBySeguidores());
        return ongs.map(OngDto::new).collect(Collectors.toList());
    }

}
