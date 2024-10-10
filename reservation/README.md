# 콘서트 예약 서비스
* [마일스톤](https://github.com/users/Lexyyaa/projects/1) 
* [시퀀스다이어그램](https://github.com/Lexyyaa/hhplus-reservation/blob/docs/reservation/doc/sequenceDiagram.md)
* [API 명세서](https://frequent-mustang-de9.notion.site/11bac8e29e2880a786a8ddcdad7ac7b4?v=11bac8e29e2881edbcce000c22c21ab1) 
* [ERD](https://github.com/Lexyyaa/hhplus-reservation/blob/docs/reservation/doc/ERD.md)
* 프로젝트 구조 - clean + layered 아키텍쳐로 구성함. 
```plaintext
    /interfaces ---------- presentation 계층 
      /api
      /dto
        /token
        /concert
        /reserve
        /point
        /payment
    /application --------- Application 계층
    /domain -------------- domain 계층 
         /token
        /concert
        /reserve
        /point
        /payment
    /infra --------------- persistance 계층
        /token
        /concert
        /reserve
        /point
        /payment
```
