package com.willy.tictactoe.service.impl;

import com.willy.tictactoe.model.Board;
import com.willy.tictactoe.repository.BoardRepository;
import com.willy.tictactoe.service.BoardService;
import org.springframework.stereotype.Service;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public void saveBoardData(Board board) {
        boardRepository.save(board);
    }

    @Override
    public Board getBoard(Long id) {
        return boardRepository.findById(id).orElse(null);
    }
}
