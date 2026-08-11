import { TestBed } from '@angular/core/testing';

import { DuvidaService } from './duvida-service';

describe('DuvidaService', () => {
  let service: DuvidaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DuvidaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
