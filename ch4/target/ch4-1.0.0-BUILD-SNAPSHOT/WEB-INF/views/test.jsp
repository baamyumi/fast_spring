<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Title</title>
  <script src="https://code.jquery.com/jquery-1.11.3.js"></script>
</head>
<body>
<h2>commentTest</h2>
comment: <input type="text" name="comment"><br>
<button id="sendBtn" type="button">등록</button>
<button id="modBtn" type="button">수정</button>
<h2>Data From Server :</h2>
<div id="commentList"></div>
<div id="replyForm" style="display: none">
  <input type="text" name="replyComment">
  <button id="wrtRepBtn" type="button">등록</button>
</div>
<script>
  let bno = 66;

  let showList = function(bno){
    $.ajax({
      type:'GET',       // 요청 메서드
      url: '/ch4/comments?bno='+ bno,  // 요청 URI
      dataType : 'json', // 전송받을 데이터의 타입  //dataType 생략하면 기본적으로 json
      success : function(result){
        //json으로 받기때문에 JSON.parse(result)가 필요없음.
        $("#commentList").html(toHtml(result));
      },
      error   : function(){ alert("error") } // 에러가 발생했을 때, 호출될 함수
    }); // $.ajax()
  }

  $(document).ready(function(){
    showList(bno);

    $("#modBtn").click(function(){
      let cno = $(this).data("cno");
      let comment = $("input[name=comment]").val();

      if(comment.trim()==''){
        alert('댓글을 입력해주세요.');
        $("input[name=comment]").focus();
        return;
      }
      $.ajax({
        type:'PATCH',       // 요청 메서드
        url: '/ch4/comments/'+cno,  // 요청 URI
        headers : { "content-type": "application/json"}, // 요청 헤더
        data : JSON.stringify({cno:cno, comment:comment}),  // 서버로 전송할 데이터. stringify()로 직렬화 필요.
        success : function(result){
          alert(result);
          showList(bno);
        },
        error:function(request,status,error){ alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error); }
      }); // $.ajax()
    });

    $("#wrtRepBtn").click(function(){
      let comment = $("input[name=replyComment]").val();
      let pcno = $('#replyForm').parent().data('cno');

      if(comment.trim()==''){
        alert('댓글을 입력해주세요.');
        $("input[name=comment]").focus();
        return;
      }
      $.ajax({
        type:'POST',       // 요청 메서드
        url: '/ch4/comments?bno='+bno,  // 요청 URI
        headers : { "content-type": "application/json"}, // 요청 헤더
        data : JSON.stringify({pcno:pcno, bno:bno, comment:comment}),  // 서버로 전송할 데이터. stringify()로 직렬화 필요.
        success : function(result){
          alert(result);
          showList(bno);
        },
        error   : function(){ alert("error") } // 에러가 발생했을 때, 호출될 함수
      }); // $.ajax()

      $('#replyForm').css('display','none');
      $("input[name=replyComment]").val('');
      $('#replyForm').appendTo("body");
    });

    $("#sendBtn").click(function(){
      let comment = $("input[name=comment]").val();

      if(comment.trim()==''){
        alert('댓글을 입력해주세요.');
        $("input[name=comment]").focus();
        return;
      }
      $.ajax({
        type:'POST',       // 요청 메서드
        url: '/ch4/comments?bno='+bno,  // 요청 URI
        headers : { "content-type": "application/json"}, // 요청 헤더
        data : JSON.stringify({bno:bno, comment:comment}),  // 서버로 전송할 데이터. stringify()로 직렬화 필요.
        success : function(result){
          alert(result);
          showList(bno);
        },
        error   : function(){ alert("error") } // 에러가 발생했을 때, 호출될 함수
      }); // $.ajax()
    });

    $("#commentList").on("click",".modBtn",function(){
      let cno = $(this).parent().data("cno");
      let comment = $("span.comment",$(this).parent()).text();

      $("input[name=comment]").val(comment);
      $("#modBtn").attr("data-cno", cno);

    });

    $("#commentList").on("click",".replyBtn",function(){
      $('#replyForm').appendTo($(this).parent());
      $('#replyForm').css("display","block");
    });

    //$(".delBtn").click(function(){  //.delBtn 동적으로 생성되는 버튼이기때문에 페이지가 로드된 시점에서 이벤트가 걸리지않음.
    // -> 이럴때는 아래처럼 이미 생성된 정적 요소에 이벤트를 걸고 클래스를 지정해줘야한다.
    $("#commentList").on("click",".delBtn",function(){
      let cno = $(this).parent().data("cno");
      let bno = $(this).parent().data("bno");

      $.ajax({
        type:'DELETE',       // 요청 메서드
        url: '/ch4/comments/'+cno+'?bno='+ bno,  // 요청 URI
        success : function(result){
          alert(result);
          showList(bno);
        },
        error:function(request,status,error){ alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error); }
      }); // $.ajax()
    });
  });
  let toHtml = function(comments){
    let tmp = "<ul>";

    comments.forEach(function (comment){
      tmp += '<li data-cno='+ comment.cno;
      tmp += ' data-pcno=' + comment.pcno;
      tmp += ' data-bno=' + comment.bno + '>';
      if(comment.cno!=comment.pcno){
        tmp += 'ㄴ';
      }
      tmp += 'commenter=<span class="commenter">' + comment.commenter + '</span>';
      tmp += ' comment=<span class="comment">' + comment.comment + '</span>';
      tmp += ' up_date=' + comment.up_date;
      tmp += '<button class="delBtn">삭제</button>';
      tmp += '<button class="modBtn">수정</button>';
      tmp += '<button class="replyBtn">답글</button>';
      tmp += '</li>';

    });
    return tmp + "</ul>";
  }
</script>
</body>
</html>