# open_api.member Requirements

## Scope

This document defines the requirements for work limited to
`com.whoz_in.main_api.query.open_api.member`.

Code changes outside this package are out of scope for the first step, but
required follow-up work is listed below.

## Goal

`open_api.member` should provide a date-based member presence query.

This is not a "today status" API and not a "member monthly block" API.

- `daily`: one list for "today"
- `block`: one member's activity grouped by days
- `open_api.member`: members grouped by requested date(s)

The response axis should be:

- `date -> members`

not:

- `member -> dates`

## Request Requirements

The query should accept:

- `year`: required
- `month`: required
- `day`: optional

Rules:

- `year` and `month` must always be provided
- `day` is optional
- if `day` is provided, return data for that exact date
- if `day` is omitted, return data for multiple dates within the requested month
- invalid calendar dates must be rejected
- future dates or future months should be handled explicitly by validation policy

## Response Requirements

The response must support both:

- one requested date
- multiple requested dates

Therefore the root response should contain a collection of date groups.

Recommended shape:

```java
public record MembersByDate(
    List<MembersOnDate> dates
) implements Response {}

public record MembersOnDate(
    LocalDate date,
    List<MemberPresence> members
) {}

public record MemberPresence(
    String memberId,
    int generation,
    String memberName,
    Duration presenceDuration
) {}
```

## Naming Requirements

`Daily` is not appropriate for this API because the query is not restricted to
"today".

Prefer names that express:

- date-based query condition
- presence history

Recommended naming direction:

- package: `application.by_date`
- query: `MembersByDateGet`
- response root: `MembersByDate`
- date group item: `MembersOnDate`
- member item: `MemberPresence`

## Boundary Requirements

`open_api.member` must not depend on `api.member` application types.

In particular:

- do not use `api.member` DTOs
- do not use `api.member` queries
- do not use `api.member` handlers
- keep `open_api.member` presentation/docs/application types independent

Shared data access may later be solved through lower-level viewers or
repositories, but the application boundary must stay explicit.

## Pagination

Pagination is not needed for this API.

Remove the idea of:

- `page`
- `size`

The request model should be driven only by:

- `year`
- `month`
- `day?`

## Terminology

Avoid `Active` as the main concept name for this API.

Reason:

- `active` usually implies "currently active now"
- this API is about historical presence on a requested date
- even when querying today, a member may have been present earlier but not be
  currently active

Preferred business term:

- `Presence`

Examples:

- `MemberPresence`
- `presenceDuration`

If current realtime status is ever needed, it should be added as a separate
field instead of defining the whole API around `Active`.

## Current Internal Issues In open_api.member

These issues are expected to be addressed while working only inside
`open_api.member`:

- current `daily` naming does not match the new requirements
- current request model still reflects old "today list" thinking
- current response model does not support multiple dates
- current handler/controller/docs are still aligned with the old daily concept
- current package contains duplicated viewer interfaces, but the usage shape is
  still not aligned with the new date-based query

## Follow-up Work Outside open_api.member

After package-internal work is done, the following external work will be
required:

1. route separation
   - move open API routes away from `/api/v1/...`
   - assign an `open-api` path prefix

2. security configuration
   - register open API routes in security filter chain configuration

3. infrastructure query support
   - add a query/viewer implementation for:
     - `date(or month) -> members`
   - current infra support is centered on:
     - today activity
     - member-based range lookup

4. viewer wiring
   - provide infra implementations for the final open API viewer interfaces if
     package-local interfaces remain

5. tests
   - request validation tests
   - controller mapping tests
   - security access tests
   - handler tests for one-day and month-range cases

