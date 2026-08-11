import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { Unauthorized } from './unauthorized';

describe('Unauthorized', () => {
  let component: Unauthorized;
  let fixture: ComponentFixture<Unauthorized>;

  let routerMock: {
    navigate: ReturnType<typeof vi.fn>;
  }

  beforeEach(async () => {
    routerMock = {
      navigate: vi.fn(),
    }

    await TestBed.configureTestingModule({
      imports: [Unauthorized],
      providers: [
        {
          provide: Router,
          useValue: routerMock
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Unauthorized);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to the route', ()=>{
    component.goTo("login");
    expect(routerMock.navigate).toHaveBeenCalledWith(['login']);
  })
});
