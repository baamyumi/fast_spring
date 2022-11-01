package com.fastcampus.ch4.controller;

import com.fastcampus.ch4.dao.CommentDao;
import com.fastcampus.ch4.domain.CommentDto;
import com.fastcampus.ch4.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController // = @Controller + @ResponseBody
public class CommentController {
    @Autowired
    CommentService service;

    /*
    * Header : Content-Type / application/json
    * {"pcno":0, "comment":"hihihi","commenter" : "asdf"
    * */
    //댓글을 수정하는 메서드
    @PatchMapping ("/comments/{cno}")   // /ch4/comments/15
    public ResponseEntity<String> modify(@PathVariable Integer cno, @RequestBody CommentDto dto, HttpSession session){
        //String commenter = (String)session.getAttribute("id");
        String commenter = "asdf";
        dto.setCommenter(commenter);
        dto.setCno(cno);

        try {
            System.out.println("dto = " + dto);
            int cnt = service.modify(dto);
            System.out.println("cnt = " + cnt);
            if (cnt!=1)
                throw new Exception("Modify failed");
            return new ResponseEntity<>("MOD_OK", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("MOD_ERR", HttpStatus.BAD_REQUEST);
        }
    }

    /*
    * Header : Content-Type / application/json
    * body : row  {"pcno":0,"comment":"hi"}
    * */
    //댓글을 등록하는 메서드
    @PostMapping("/comments")   // /ch4/comments?bno=66
    public ResponseEntity<String> write(@RequestBody CommentDto dto, Integer bno, HttpSession session){
        //String commenter = (String)session.getAttribute("id");
        String commenter = "asdf";
        dto.setCommenter(commenter);
        dto.setBno(bno);

        try {
            if (service.write(dto)!=1)
                throw new Exception("Write failed");
            return new ResponseEntity<>("WRT_OK", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("WRT_ERR", HttpStatus.BAD_REQUEST);
        }
    }

    //지정된 댓글을 삭제하는 메서드
    @DeleteMapping("/comments/{cno}")  // /comments/1?bno=66 <-- 삭제할 댓글 번호
    public ResponseEntity<String> remove(@PathVariable Integer cno, Integer bno, HttpSession session){
        //String commenter = (String)session.getAttribute("id");
        String commenter = "asdf";
        try {
            int rowCnt = service.remove(cno, bno, commenter);
            System.out.println("Controller ::: rowCnt = " + rowCnt);
            if (rowCnt!=1)
                throw new Exception("Delete Failed");

            return new ResponseEntity<>("DEL_OK",HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("DEL_ERR",HttpStatus.BAD_REQUEST);
        }
    }

    //지정된 게시물의 모든 댓글을 가져오는 메서드
    @GetMapping("/comments")    //  /comments?bno=1000
    public ResponseEntity<List<CommentDto>> list(Integer bno){
        List<CommentDto> list = null;
        try {
            list = service.getList(bno);
            return new ResponseEntity<List<CommentDto>>(list, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<CommentDto>>(HttpStatus.BAD_REQUEST);
        }
    }
}
