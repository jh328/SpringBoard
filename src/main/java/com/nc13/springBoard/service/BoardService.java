package com.nc13.springBoard.service;

import com.nc13.springBoard.model.BoardDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class BoardService {
    private final String NAMESPACE = "com.nc13.mappers.BoardMapper";
    // 한 페이지에 들어갈 글의 갯수
    private final int PAGE_SIZE = 25;

    @Autowired
    private SqlSession session;


    public List<BoardDTO> selectAll(int pageNo) {
        // pageNo가 1일 때는
        // 0번째 로우 부터 25개를 뽑아야 한다.
        // pageNo가 2일 때는
        // 25번째 로우부터는 25개를 뽑아야 한다.
        // pageNo 가 3일 때는
        // 50번째 로우부터 25개를 뽑아야 한다.
        // pageNo가 n 일때는
        // (n-1) * 25번째 로우부터 25개를 뽑아야 함.
        // 우리가 이번에는 Mapper.xml 으로 2가지 값을 넘겨 주어야 한다.
        // 이때에는 따로 DTO를 만들어줘도 되지만
        // Map을 넘겨줘도 된다.
        HashMap<String, Integer> paramMap = new HashMap<>();
        paramMap.put("startRow", (pageNo - 1) * PAGE_SIZE);
        paramMap.put("size", PAGE_SIZE);

        return session.selectList(NAMESPACE + ".selectAll",paramMap);
    }

    public void insert(BoardDTO boardDTO) {
        System.out.println("insert전 boardDTO:" + boardDTO);
        session.insert(NAMESPACE + ".insert", boardDTO);
        System.out.println("insert후 boardDTO:" + boardDTO);
    }

    public BoardDTO selectOne(int id){
        return session.selectOne(NAMESPACE +".selectOne", id);
    }

    public void update(BoardDTO attempt){
        session.update(NAMESPACE+".update", attempt);
    }

    public void delete(int id){
        session.delete(NAMESPACE + ".delete", id);
    }

    public int selectMaxPage(){
        // 글의 총 갯수
        int maxRow = session.selectOne(NAMESPACE + ".selectMaxRow");

        // 총 페이지 갯수
        int maxPage = maxRow / PAGE_SIZE;

        if (maxRow % PAGE_SIZE != 0) {
            maxPage++;
        }

        // 게시판 마지막 페이지가 결정이 되는 코드
        return maxPage;
    }

}
