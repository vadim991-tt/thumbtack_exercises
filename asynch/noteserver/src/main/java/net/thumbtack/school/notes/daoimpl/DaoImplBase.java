package net.thumbtack.school.notes.daoimpl;


import net.thumbtack.school.notes.mappers.AuthorMapper;
import net.thumbtack.school.notes.mappers.CommentMapper;
import net.thumbtack.school.notes.mappers.NoteMapper;
import net.thumbtack.school.notes.mappers.SectionMapper;
import net.thumbtack.school.notes.utils.ConfigProperties;
import net.thumbtack.school.notes.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

@Component
public class DaoImplBase {

    public SqlSession getSession() {
        return MyBatisUtils.getSqlSessionFactory().openSession();
    }

    public AuthorMapper getAuthorMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(AuthorMapper.class);
    }

    public NoteMapper getNoteMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(NoteMapper.class);
    }

    public SectionMapper getSectionMapper(SqlSession sqlSession){return  sqlSession.getMapper(SectionMapper.class);}

    public CommentMapper getCommentMapper(SqlSession sqlSession){return  sqlSession.getMapper(CommentMapper.class);}
}