# Social Network Console — Java 21

A console-based social networking application implementing posting, reading,
following, and wall aggregation.
## Commands

| Input                             | Action                     |
|-----------------------------------|----------------------------|
| `Alice -> I love the weather`     | Post to Alice's timeline   |
| `Alice`                           | Read Alice's timeline      |
| `Charlie follows Alice`           | Charlie subscribes to Alice|
| `Charlie wall`                    | View Charlie's wall        |
| `exit`                            | Quit the application       |

---

## Example Session

```
> Alice -> I love the weather today
> Bob -> Damn! We lost!
> Bob -> Good game though.
> Alice
I love the weather today (5 minutes ago)
> Bob
Good game though. (1 minute ago)
Damn! We lost! (2 minutes ago)
> Charlie -> I'm in New York today! Anyone want to have a coffee?
> Charlie follows Alice
> Charlie follows Bob
> Charlie wall
Charlie - I'm in New York today! Anyone want to have a coffee? (15 seconds ago)
Bob - Good game though. (1 minute ago)
Bob - Damn! We lost! (2 minutes ago)
Alice - I love the weather today (5 minutes ago)
```

---


### Design Principles

- **Command** — mapping each user input to a`Command` object.
  Adding a new command requires implementing `Command` and registering a parser rule.
- **Repository** — storage is abstracted behind interfaces, enabling easy
  swap to different storage without touching business logic.
- **Service** — the business logic is contained in the Service class.
- **Utility classes** — static classes (utility) to help with some of the logic.
---


