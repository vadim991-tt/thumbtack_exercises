package net.thumbtack.school.notes.mapstruct;

import net.thumbtack.school.notes.dto.request.author.RegisterAuthorDtoRequest;
import net.thumbtack.school.notes.dto.response.author.*;
import net.thumbtack.school.notes.model.Author;
import net.thumbtack.school.notes.model.Role;
import net.thumbtack.school.notes.view.AuthorView;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DtoAuthorMapper {

    DtoAuthorMapper INSTANCE = Mappers.getMapper(DtoAuthorMapper.class);

    RegisterAuthorDtoResponse responseFromRequest(RegisterAuthorDtoRequest request);

    Author authorFromRegisterDto(RegisterAuthorDtoRequest request);

    RegisterAuthorDtoResponse responseFromAuthor(Author author);

    AuthorInfoDtoResponse infoFromAuthor(Author author);

    ChangePasswordDtoResponse changePassDtoFromAuthor(Author author);

    @IterableMapping(qualifiedByName = "author")
    List<AuthorDtoResponse> authorsFromViews(List<AuthorView> views);

    @IterableMapping(qualifiedByName = "authorBySuper")
    List<AuthorBySuperDtoResponse> authorsBySuperFromViews(List<AuthorView> views);

    @Named("author")
    @Mapping(source = "uuid", target = "online", qualifiedByName = "uuidToBool")
    AuthorDtoResponse authorResponseFromView(AuthorView view);

    @Named("authorBySuper")
    @Mapping(source = "uuid", target = "online", qualifiedByName = "uuidToBool")
    @Mapping(source = "role", target = "isSuper", qualifiedByName = "roleToBool")
    AuthorBySuperDtoResponse authorBySuperResponseFromView(AuthorView view);

    @Named("roleToBool")
    static boolean setIsSuper(Role role) {
        return role == Role.SUPER;
    }

    @Named("uuidToBool")
    static boolean isOnline(String uuid) {
        return uuid != null;
    }

}
