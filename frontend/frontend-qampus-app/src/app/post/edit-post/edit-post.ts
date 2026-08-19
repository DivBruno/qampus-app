import { Component, OnInit } from '@angular/core';
import { Navbar } from '../../navbar/navbar';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService, Post, EditPostI } from '../post-service';
import { FormControl, FormGroup, FormsModule, Validators, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-edit-post',
  imports: [Navbar, FormsModule, ReactiveFormsModule],
  templateUrl: './edit-post.html',
  styleUrl: './edit-post.css',
})
export class EditPost implements OnInit {
  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
    private router: Router
  ){}
  post: Post = {
    id: '',
    title: '',
    content: '',
    upVotes: 0,
    downVotes: 0,
    tags: [],
    createdAt: ''
  }
  novaTag = false;
  nomeTag = '';
  quantidadeTags = 0;
  tagsCriadas: string[] = [];
  postForm = new FormGroup({
    title: new FormControl('', Validators.required),
    content: new FormControl('', Validators.required)
  })
    abrirFecharTag() {
    this.novaTag = !this.novaTag;
  }

  adicionarTag() {
    if (this.nomeTag.trim() !== '') {
        this.tagsCriadas.push(this.nomeTag.trim());
        this.nomeTag = '';
        this.novaTag = false;
    }
  }

  goTo(rota: string){
    this.router.navigate([rota]);
  }

  removeTag(tag: string){
    const tagRemovida = this.tagsCriadas.indexOf(tag);
    this.tagsCriadas.splice(tagRemovida, 1);
  }
  async ngOnInit(){
    const id = this.route.snapshot.paramMap.get('id');

    if(id){
      const postEncontrado = await this.postService.findById(id);
      if(postEncontrado){
        this.post = postEncontrado
        this.postForm.setValue({
          title: this.post.title,
          content: this.post.content
        })
        for (let index = 0; index < this.post.tags.length; index++) {
          this.nomeTag = this.post.tags[index].name;
          this.adicionarTag();
        }
      }
    }
  }
  async submit(){
    const editPost:EditPostI = {
      id: this.post.id,
      title: this.postForm.value.title!,
      content: this.postForm.value.content!,
      tags: this.tagsCriadas
    }
    const response = await this.postService.editPost(editPost, editPost.id);
    if(response){
      this.router.navigate(['home']);
    }else{
      alert("Erro ao editar a dúvida")
    }
  }
}
