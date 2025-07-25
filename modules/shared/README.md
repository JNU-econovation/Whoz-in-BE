# 📦 `shared` 모듈 관리 가이드

## 🧩 개요

`shared` 모듈은 **대부분의 모듈에서 공통적으로 알아야 하는 개념**들을 모아두는 공간입니다.  
이 모듈을 통해 중복을 제거하고 서비스 전반의 일관성을 유지할 수 있습니다.

---

## ✅ 포함 가능한 항목 예시
- 도메인 이벤트
    - 도메인 이벤트는 여러 도메인 혹은 레이어 간 통신을 위한 계약으로서 여러 곳에서 사용될 수 있기 때문입니다. 
- 공용 상수 / Enum / annotation
    - `@Nullable` 등
- 공용 커스텀 예외 클래스
    - `WhozinException`, `ErrorCode` 등

---

## ❌ 포함되면 안 되는 항목 예시

- 특정 모듈에만 관련된 클래스
    - `CommandHandler`: api 모듈에서만 사용하는 클래스
---

## ⚠️ 관리 주의사항

- `shared`는 **모든 모듈이 참조할 수 있는 위치**이므로, 무분별하게 커지지 않도록 주의해야 합니다.
- 아래와 같은 문제점이 발생할 수 있으므로, 공통 목적이 명확하지 않은 경우 절대 클래스를 추가하지 않습니다.
    
    | 문제점             | 설명 |
    |------------------|------|
    | 의존성 꼬임         | 특정 도메인의 내부 객체가 shared에 들어오면 의존성이 뒤섞임 |
    | 변경 영향 증가       | 하나의 변경이 여러 모듈에 연쇄적으로 영향을 줄 수 있음 |
    | 책임 불명확         | 도메인 특화 객체가 shared에 들어오면 목적이 모호해짐 |

- 의존성은 반드시 **최소한으로 유지**해야 하며, **필수적인지** 잘 검토하여 추가합니다.
- 클래스 상단에 목적을 명시하는 주석을 작성하면 좋을듯 합니다.