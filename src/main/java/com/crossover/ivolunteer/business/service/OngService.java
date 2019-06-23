package com.crossover.ivolunteer.business.service;

import com.crossover.ivolunteer.business.entity.Ong;
import com.crossover.ivolunteer.data.repository.OngRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.stream.Stream;


@Service
@Transactional
public class OngService extends EntityServiceBase<Ong, Long, OngRepository> {

    static class OngComparatorBySeguidores implements Comparator<Ong> {
        public int compare(Ong c1, Ong c2) {
            return c1.getSeguidores().size() - c2.getSeguidores().size();
        }
    }

    public Stream<Ong> sort(Stream<Ong> ongs) {
        return ongs.sorted(new OngComparatorBySeguidores());
    }

}
