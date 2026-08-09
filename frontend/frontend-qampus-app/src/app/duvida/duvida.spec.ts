import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Duvida } from './duvida';

describe('Duvida', () => {
  let component: Duvida;
  let fixture: ComponentFixture<Duvida>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Duvida],
    }).compileComponents();

    fixture = TestBed.createComponent(Duvida);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
