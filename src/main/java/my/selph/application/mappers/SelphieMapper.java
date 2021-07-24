package my.selph.application.mappers;

import my.selph.application.dtos.SelphieGet;
import my.selph.application.dtos.SelphiePost;
import my.selph.domain.entities.Selphie;
import org.mapstruct.Mapper;

import java.io.File;
import java.util.Random;

@Mapper(componentModel = "cdi")
public interface SelphieMapper {

    Selphie fromPost(SelphiePost source);

    SelphieGet fromEntity(Selphie source);

    default String fileToString(File source) {
        return (new Random().nextInt())+".mp3";
    }
}
