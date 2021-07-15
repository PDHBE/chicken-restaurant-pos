# 요구사항

<img width="818" alt="스크린샷 2021-07-03 오후 1 59 31" src="https://user-images.githubusercontent.com/82703938/124346963-ad529e80-dc1c-11eb-83e8-c604da614c87.png">
<img width="970" alt="스크린샷 2021-07-03 오후 2 00 01" src="https://user-images.githubusercontent.com/82703938/124346974-b3e11600-dc1c-11eb-9c4e-06db3b17587c.png">

# 구조

<img width="760" alt="스크린샷 2021-07-03 오후 4 58 20" src="https://user-images.githubusercontent.com/82703938/124347473-f821e580-dc1f-11eb-8cba-315d19076312.png">
<img width="320" alt="스크린샷 2021-07-03 오후 5 00 53" src="https://user-images.githubusercontent.com/82703938/124347569-48994300-dc20-11eb-9bcf-91c93d5e6a76.png">

# 이슈

## 도메인 주도 설계 구조로 변경

- 개발을 할때마다 구성 요소들을 어떤 패키지 구조로 설계해야 하는지에 대해 항상 고민해왔다. 그러다 DDD START! 라는 책을 보며 도메인 주도적으로 설계하는 것에 대해 알 수 있었고, 조금이나마 그 답을 찾을 수 있었다. 이에 따라, 기존에 개발했었던 구조에서 도메인 주도 설계 구조로 완전히 뜯어 고쳐 보았다. 

- 기븐적으로는 표현(ui) 계층, 응용(application) 계층, 도메인(domain) 계층, 인프라 스트럭쳐(infra structure) 계층의 4 계층으로 구성되어 있으며, 도메인의 크기가 커지면 도메인들이 최상위 계층을 이루며 도메인마다 4 계층을 가지는 구조로도 만들 수 있다.

- 본 프로젝트엔 특별한 구현 기술이 없어 인프라 스트럭쳐 계층을 제외하였고, 표현(ui) 계층은 다시 view, controller 계층으로 나누었고, 도메인(domain) 계층은 다시 model, repository, service 계층으로 나누었다.

## 계층간 의존성 분리

- view에서 테이블 목록을 보여줄때, 주문이 있는 테이블은 따로 표시가 되어 있어야 한다. 

- 최초엔 이를 구현하기 위하여 view 계층에서 domain 계층의 TableRepository에 접근하여 모든 테이블 목록을 가져왔고, OrderRepository에 접근하여 주문이 있는 테이블 목록을 가져왔다.

- 하지만 이렇게 되면 view 계층에서 곧바로 DB에 접근하는 셈이 되어버린 것이다. 따라서, controller 계층에서 DB에 접근하여 조회한 결과를 view에 전달하는 식으로 변경하였다.

- ( 원래 도메인 DB(repository)에 접근하는 계층은 응용(application) 계층이지만, 트랜잭션 처리가 필요없는 단순 조회의 경우 그 앞 단계 계층인 controller 계층에서 수행하도록 하는 것이 효율적이다. )

## 값 검증 계층

- 원칙적으로는 모든 값의 검증은 응용 계층에서 처리해야 한다. 이 때, view의 입력값이 여러개인 경우엔 이 값들을 한번에 응용 계층으로 넘겨 검증을 받게 되고, 한 입력값에 대해서 예외가 발생하면 그 뒤 입력값들은 검증받지 못하게 된다. 이는 사용자가 여러번 입력하게 만드는 불편함을 야기한다. 

- 따라서 값 검증을 효율적으로 처리하기 위하여 이를 표현 계층의 controller가 처리하도록 구현하였다. 또한, 입력받은 테이블 번호에 해당하는 테이블이 존재하는지와 같은 존재 여부 검증을 위해서는 DB(repository)에 접근해야하는데, 이 역시 트랜잭션 처리가 필요없는 단순 조회 기능이므로 응용 계층에 이를 위한 서비스를 따로 만들지 않고, controller에서 바로 repository에 접근하도록 구현하였다.

## 조금 더 객체지향적으로

- 주문의 총 금액을 계산하기 위해서 최초엔 getter로 메뉴의 가격과 수량에 접근하여 계산하도록 구현하였다. 하지만 이렇게 되면 주문 객체는 스스로 데이터를 처리하지 못하고, 단지 그대로 데이터를 노출시킴으로써 외부에서 이를 처리하도록 책임을 떠넘기는 것이다. 따라서 이 계산 책임을 스스로 처리하여 그 결과를 반환하도록 변경하였다.

- 수동적인 getter 인터페이스를 제거하고, 스스로 총 금액을 계산하여 calculate 인터페이스로 리턴하도록 변경하였다.

- 또한, 주문의 메뉴가 치킨 카테고리인지 판단하는 것을 기존에는 getter로 외부에서 처리하였지만, 스스로 상태를 체크하여 isChicken 인터페이스로 진위 여부를 리턴하도록 변경하였다. 
