package net.thumbtack.school.notes.daoimpl;

import net.thumbtack.school.notes.base.ServerErrorCode;
import net.thumbtack.school.notes.base.ServerException;
import net.thumbtack.school.notes.model.Author;
import net.thumbtack.school.notes.model.Comment;
import net.thumbtack.school.notes.dao.CommentDao;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Qualifier("commentDaoImpl")
public class CommentDaoImpl extends DaoImplBase implements CommentDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorDaoImpl.class);

    @Override
    public Comment insertComment(Author author, Comment comment, int noteId) throws ServerException {
        LOGGER.info("Insert comment [body:{}] to note with {} id", comment.getBody(), noteId);
        try (SqlSession sqlSession = getSession()) {
            try {
                getCommentMapper(sqlSession).insert(comment, author.getId(), noteId);
                comment = getCommentMapper(sqlSession).getById(comment.getId());
            } catch (RuntimeException e) {
                LOGGER.info("Can't insert comment {}", comment.getBody(), e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return comment;
    }

    @Override
    public List<Comment> getComments(Author author, int noteId) throws ServerException {
        LOGGER.info("DAO get comments");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getCommentMapper(sqlSession).getByNoteId(noteId);
            } catch (RuntimeException e) {
                LOGGER.info("Can't get comments ", e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public Comment changeBody(Author author, int id, String newBody) throws ServerException {
        LOGGER.info("DAO change comment body. ID: {}", id);
        Comment comment;
        try (SqlSession sqlSession = getSession()) {
            try {
                getCommentMapper(sqlSession).changeBody(id, newBody, author.getId());
                comment = getCommentMapper(sqlSession).getById(id);
            } catch (RuntimeException e) {
                LOGGER.info("Can't change comment body ", e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return comment;
    }

    @Override
    public void delete(Author author, int id) throws ServerException {
        LOGGER.info("DAO delete comment");
        try (SqlSession sqlSession = getSession()) {
            try {
                getCommentMapper(sqlSession).delete(id, author.getId());
            } catch (RuntimeException e) {
                LOGGER.info("Can't change comment body ", e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

    @Override
    public void deleteFromNote(Author author, int noteId) throws ServerException {
        LOGGER.info("DAO delete comments from note");
        try (SqlSession sqlSession = getSession()) {
            try {
                getCommentMapper(sqlSession).deleteFromNote(noteId, author.getId());
            } catch (RuntimeException e) {
                LOGGER.info("Can't delete comments from note ", e);
                throw new ServerException(ServerErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

}
